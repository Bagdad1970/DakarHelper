package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.model.FileStatus;
import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.dakarhelperservice.service.interfaces.ParserService;
import io.github.bagdad.dakarhelperservice.service.interfaces.HeaderCellService;
import io.github.bagdad.dakarhelperservice.service.interfaces.ProductService;
import io.github.bagdad.dakarhelperservice.service.interfaces.SubcategoryService;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorFileService;
import io.github.bagdad.dakarhelperservice.service.interfaces.VendorService;
import io.github.bagdad.emailhandler.config.EmailConfig;
import io.github.bagdad.emailhandler.EmailHandler;
import io.github.bagdad.dakarhelperservice.helper.EmailHelper;
import io.github.bagdad.dakarhelperservice.helper.ExcelParserHelper;
import io.github.bagdad.excelparser.ExcelParser;
import io.github.bagdad.excelparser.model.ExcelProduct;
import io.github.bagdad.excelparser.headerparser.ParserFactory;
import io.github.bagdad.excelparser.utils.ExcelWorkbookReader;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.findhandler.FileHandler;
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
public class ExcelParserServiceImpl implements ParserService {

    private final VendorService vendorService;

    private final VendorFileService vendorFileService;

    private final HeaderCellService headerCellService;

    private final ProductService productService;

    private final SubcategoryService subcategoryService;

    public ExcelParserServiceImpl(VendorService vendorService,
                                  VendorFileService vendorFileService,
                                  HeaderCellService headerCellService,
                                  ProductService productService,
                                  SubcategoryService subcategoryService
    ) {
        this.vendorService = vendorService;
        this.vendorFileService = vendorFileService;
        this.headerCellService = headerCellService;
        this.productService = productService;
        this.subcategoryService = subcategoryService;
    }

    @Override
    public void parse() {
        synchronizeExcelFiles();

        parseExcelFiles();

        deleteProcessedFilesFromFilesystem();
    }

    private void deleteProcessedFilesFromFilesystem() {
        List<VendorFile> parsedVendorFiles = vendorFileService.findByFileStatus(FileStatus.PARSED);

        for (VendorFile vendorFile : parsedVendorFiles) {
            FileHandler.deleteFile(vendorFile.getFilepath());
        }
    }

    private void removeOldVendorFilesAndData(List<VendorWithFilepathes> oldVendors) {
        for (VendorWithFilepathes oldVendor : oldVendors) {
            Long oldVendorId = oldVendor.getId();

            vendorFileService.deleteByVendorId(oldVendorId);

            List<VendorFile> vendorFilesToDelete = vendorFileService.findByVendorId(oldVendorId);

            vendorFilesToDelete.forEach(vendorFile -> productService.deleteByVendorId(vendorFile.getVendorId()));
        }
    }

    private void synchronizeExcelFiles() {
        List<VendorWithMaxFileDateTime> vendorsWithLastFileTimestamp = vendorService.findVendorsWithLastFileDate();

        if (vendorsWithLastFileTimestamp.isEmpty()) {
            return;
        }

        EmailHandler emailHandler = new EmailHandler(vendorsWithLastFileTimestamp);

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
        List<HeaderCell> headerCellsWithArticleCategory = headerCellService.findAllByCategory(Category.ARTICLE);

        List<Subcategory> excelHeaderSubcategories = subcategoryService.findAll();

        ParserFactory parserFactory = new ParserFactory();
        SubcategoryMapping nameMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithNameCategory, excelHeaderSubcategories);
        SubcategoryMapping priceMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithPriceCategory, excelHeaderSubcategories);
        SubcategoryMapping quantityMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithQuantityCategory, excelHeaderSubcategories);
        SubcategoryMapping articleMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithArticleCategory, excelHeaderSubcategories);

        parserFactory.addMapping(Category.NAME, nameMapping);
        parserFactory.addMapping(Category.PRICE, priceMapping);
        parserFactory.addMapping(Category.QUANTITY, quantityMapping);
        parserFactory.addMapping(Category.ARTICLE, articleMapping);

        return parserFactory;
    }

    private ExcelHeaderCellsHandler createExcelHeaderCellsHandler() {
        List<HeaderCellDto> excelHeaderCellsWithSubcategory = ExcelParserHelper.mapToHeaderCellDtos(headerCellService.findAllWithSubcategory());
        return new ExcelHeaderCellsHandler(excelHeaderCellsWithSubcategory);
    }

    private void parseExcelFiles() {
        ParserFactory parserFactory = createParserFactory();

        ExcelHeaderCellsHandler excelHeaderCellsHandler = createExcelHeaderCellsHandler();

        List<VendorFile> unparsedVendorFiles = vendorFileService.findByFileStatus(FileStatus.CREATED);

        Map<Long, List<VendorFile>> vendorFilesByVendorId = unparsedVendorFiles.stream()
                .collect(Collectors.groupingBy(VendorFile::getVendorId));

        for (List<VendorFile> vendorFileGroup : vendorFilesByVendorId.values()) {
            parseFileGroupOfVendor(vendorFileGroup, excelHeaderCellsHandler, parserFactory);
        }
    }

    public void parseFileGroupOfVendor(List<VendorFile> vendorFileGroup, ExcelHeaderCellsHandler excelHeaderCellsHandler, ParserFactory parserFactory) {
        if (vendorFileGroup.isEmpty()) {
            return;
        }

        Set<Product> uniqueProducts = new HashSet<>();
        for (VendorFile vendorFile : vendorFileGroup) {
            log.info("Parsing file: {}", vendorFile.getFilepath());

            try (ExcelWorkbookReader excelWorkbookReader = new ExcelWorkbookReader(vendorFile.getFilepath())) {
                ExcelParser excelParser = new ExcelParser(excelWorkbookReader.getFirstSheet(), excelHeaderCellsHandler, parserFactory);

                boolean containsUnprocessableHeaderCells = excelParser.tryToParse();

                if (containsUnprocessableHeaderCells) {
                    List<HeaderCellDto> unprocessableHeaderCellDtos = excelParser.getUnprocessableHeaderCells();
                    if (!unprocessableHeaderCellDtos.isEmpty()) {
                        List<HeaderCell> unprocessableHeaderCells = ExcelParserHelper.mapToHeaderCells(unprocessableHeaderCellDtos);

                        headerCellService.batchInsert(unprocessableHeaderCells);

                        // send to a user

                        // get answers by user and update the DB

                        // get new headerCells and set them into the ExcelHeaderCells table
                    }

                    excelParser.processUnprocessedCells();
                }

                List<ExcelProduct> foundProducts = excelParser.parse();

                if (!foundProducts.isEmpty()) {
                    List<Product> products = ExcelParserHelper.mapToProducts(vendorFile.getVendorId(), foundProducts);

                    uniqueProducts.addAll(products);
                }
            }
        }

        for (VendorFile vendorFile : vendorFileGroup) {
            vendorFile.setFileStatus(FileStatus.PARSED);
            vendorFileService.update(vendorFile);
        }

        productService.saveAll(uniqueProducts);
    }

}
