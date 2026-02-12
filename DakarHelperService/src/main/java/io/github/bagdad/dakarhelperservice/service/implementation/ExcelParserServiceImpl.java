package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.model.FileStatus;
import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.Storage;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.emailhandler.EmailConfig;
import io.github.bagdad.emailhandler.EmailHandler;
import io.github.bagdad.dakarhelperservice.helper.EmailHelper;
import io.github.bagdad.dakarhelperservice.helper.ExcelParserHelper;
import io.github.bagdad.dakarhelperservice.service.interfaces.*;
import io.github.bagdad.excelparser.ExcelParser;
import io.github.bagdad.excelparser.model.ExcelProduct;
import io.github.bagdad.excelparser.headerparser.ParserFactory;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.emailhandler.VendorWithFilepathes;
import io.github.bagdad.models.emailhandler.VendorWithMaxFileDateTime;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExcelParserServiceImpl implements ExcelParserService {

    private final EmailConfig emailConfig;

    private final VendorService vendorService;

    private final VendorFileService vendorFileService;

    private final HeaderCellService headerCellService;

    private final ProductService productService;

    private final StorageService storageService;

    private final SubcategoryService subcategoryService;

    public ExcelParserServiceImpl(EmailConfig emailConfig,
                                  VendorService vendorService,
                                  VendorFileService vendorFileService,
                                  HeaderCellService headerCellService,
                                  ProductService productService,
                                  StorageService storageService,
                                  SubcategoryService subcategoryService
    ) {
        this.emailConfig = emailConfig;
        this.vendorService = vendorService;
        this.vendorFileService = vendorFileService;
        this.headerCellService = headerCellService;
        this.productService = productService;
        this.storageService = storageService;
        this.subcategoryService = subcategoryService;
    }

    @Override
    public void runExcelParser() {
        synchronizeExcelFiles();

        parseExcelFiles();
    }

    private void removeOldVendorFilesAndData(List<VendorWithFilepathes> oldVendors) {
        for (VendorWithFilepathes oldVendor : oldVendors) {
            Long oldVendorId = oldVendor.getId();

            vendorFileService.deleteByVendorId(oldVendorId);

            List<VendorFile> vendorFilesToDelete = vendorFileService.findByVendorId(oldVendorId);

            for (VendorFile vendorFile : vendorFilesToDelete) {
                Long vendorFileId = vendorFile.getId();

                productService.deleteByVendorFileId(vendorFileId);
                storageService.deleteByVendorFileId(vendorFileId);
            }
        }
    }

    private void synchronizeExcelFiles() {
        List<VendorWithMaxFileDateTime> vendorsWithLastFileTimestamp = vendorService.findVendorsWithLastFileDate();

        if (vendorsWithLastFileTimestamp.isEmpty()) {
            return;
        }

        EmailHandler emailHandler = new EmailHandler(emailConfig, vendorsWithLastFileTimestamp);

        List<VendorWithFilepathes> vendorsWithFilepathes = emailHandler.readEmail();

        removeOldVendorFilesAndData(vendorsWithFilepathes);

        List<VendorFile> vendorFiles = EmailHelper.mapVendorFilesAndVendors(vendorsWithFilepathes, vendorsWithLastFileTimestamp);

        if (!vendorFiles.isEmpty()) {
            vendorFileService.batchInsert(vendorFiles);
        }
    }

    private ParserFactory createParserFactory() {
        List<HeaderCell> headerCellsWithNameCategory = headerCellService.findAllByCategory(Category.NAME);
        List<HeaderCell> headerCellsWithPriceCategory = headerCellService.findAllByCategory(Category.PRICE);
        List<HeaderCell> headerCellsWithQuantityCategory = headerCellService.findAllByCategory(Category.QUANTITY);

        List<Subcategory> excelHeaderSubcategories = subcategoryService.findAll();

        SubcategoryMapping nameMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithNameCategory, excelHeaderSubcategories);
        SubcategoryMapping priceMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithPriceCategory, excelHeaderSubcategories);
        SubcategoryMapping quantityMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithQuantityCategory, excelHeaderSubcategories);
        return new ParserFactory(nameMapping, priceMapping, quantityMapping);
    }

    private ExcelHeaderCellsHandler createExcelHeaderCellsHandler() {
        List<HeaderCellDto> excelHeaderCellsWithSubcategory = ExcelParserHelper.mapToExcelHeaderCellDtos(headerCellService.findAllWithSubcategory());
        return new ExcelHeaderCellsHandler(excelHeaderCellsWithSubcategory);
    }

    private void parseExcelFiles() {
        ParserFactory parserFactory = createParserFactory();

        ExcelHeaderCellsHandler excelHeaderCellsHandler = createExcelHeaderCellsHandler();

        List<VendorFile> unparsedVendorFiles = vendorFileService.findByFileStatus(FileStatus.CREATED);

        Map<Long, List<VendorFile>> vendorFilesByVendorId = unparsedVendorFiles.stream()
                .collect(Collectors.groupingBy(VendorFile::getVendorId));

        for (List<VendorFile> vendorFileGroup : vendorFilesByVendorId.values()) {
            parseFileGroup(vendorFileGroup, excelHeaderCellsHandler, parserFactory);
        }

        log.info("Parsing ended");
    }

    public void parseFileGroup(List<VendorFile> vendorFileGroup, ExcelHeaderCellsHandler excelHeaderCellsHandler, ParserFactory parserFactory) {
        if (vendorFileGroup.isEmpty()) {
            return;
        }

        Set<Product> uniqueProducts = new HashSet<>();
        List<Storage> storages = new ArrayList<>();
        for (VendorFile vendorFile : vendorFileGroup) {
            log.info("Parsing file: {}", vendorFile.getFilepath());

            ExcelParser excelParser = new ExcelParser(vendorFile.getFilepath(), excelHeaderCellsHandler, parserFactory);

            boolean containsUnprocessableHeaderCells = excelParser.tryToParse();

            if (containsUnprocessableHeaderCells) {
                List<HeaderCellDto> unprocessableHeaderCellDtos = excelParser.getUnprocessableHeaderCells();
                if (!unprocessableHeaderCellDtos.isEmpty()) {
                    List<HeaderCell> unprocessableHeaderCells = ExcelParserHelper.mapToExcelHeaderCells(unprocessableHeaderCellDtos);

                    headerCellService.batchInsert(unprocessableHeaderCells);

                    // send to a user

                    // get answers by user and update the DB

                    // get new headerCells and set them into the ExcelHeaderCells table
                }

                excelParser.processUnprocessedCells();
            }

            List<ExcelProduct> foundProducts = excelParser.parse();

            if (!foundProducts.isEmpty()) {
                List<Product> products = ExcelParserHelper.mapToExcelProducts(vendorFile, foundProducts);

                uniqueProducts.addAll(products);

                Storage storage = Storage.builder()
                        .vendorFileId(vendorFile.getId())
                        .storages(excelParser.getStorages())
                        .build();
                storages.add(storage);
            }
        }

        for (VendorFile vendorFile : vendorFileGroup) {
            vendorFile.setFileStatus(FileStatus.PARSED);
            vendorFileService.update(vendorFile);
        }

        productService.saveAll(uniqueProducts);

        storageService.saveAll(storages);
    }

}
