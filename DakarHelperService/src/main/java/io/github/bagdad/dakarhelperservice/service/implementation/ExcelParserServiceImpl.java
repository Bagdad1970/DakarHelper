package io.github.bagdad.dakarhelperservice.service.implementation;

import emailhandler.EmailConfig;
import emailhandler.EmailHandler;
import io.github.bagdad.dakarhelperservice.helper.EmailHelper;
import io.github.bagdad.dakarhelperservice.helper.ExcelParserHelper;
import io.github.bagdad.dakarhelperservice.model.*;
import io.github.bagdad.dakarhelperservice.service.interfaces.*;
import io.github.bagdad.excelparser.ExcelParser;
import io.github.bagdad.excelparser.model.Product;
import io.github.bagdad.excelparser.headerparser.ParserFactory;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelParserServiceImpl implements ExcelParserService {

    private final EmailConfig emailConfig;

    private final VendorServiceImpl vendorService;

    private final VendorFileService vendorFileService;

    private final HeaderCellService headerCellService;

    private final ExcelProductService excelProductService;

    private final ExcelStorageService excelStorageService;

    private final SubcategoryService subcategoryService;

    public ExcelParserServiceImpl(EmailConfig emailConfig,
                                  VendorServiceImpl vendorService,
                                  VendorFileService vendorFileService,
                                  HeaderCellService headerCellService,
                                  ExcelProductService excelProductService,
                                  ExcelStorageService excelStorageService,
                                  SubcategoryService subcategoryService
    ) {
        this.emailConfig = emailConfig;
        this.vendorService = vendorService;
        this.vendorFileService = vendorFileService;
        this.headerCellService = headerCellService;
        this.excelProductService = excelProductService;
        this.excelStorageService = excelStorageService;
        this.subcategoryService = subcategoryService;
    }

    @Override
    public void runExcelParser() {
        synchronizeExcelFiles();

        parseExcelFiles();
    }

    private void synchronizeExcelFiles() {
        List<Vendor> vendors = vendorService.findAll();

        List<String> vendorTitles = vendors.stream()
                .map(Vendor::getTitle)
                .toList();

        if (vendorTitles.isEmpty()) {
            return;
        }

        EmailHandler emailHandler = new EmailHandler(emailConfig, vendorTitles);

        emailHandler.run();

        List<VendorFile> vendorFiles = EmailHelper.mapVendorFilesAndVendors(emailHandler, vendors);

        vendorFileService.batchInsert(vendorFiles);
    }

    private void parseExcelFiles() {
        List<HeaderCell> headerCellsWithNameCategory = headerCellService.findAllByCategory(Category.NAME);
        List<HeaderCell> headerCellsWithPriceCategory = headerCellService.findAllByCategory(Category.PRICE);
        List<HeaderCell> headerCellsWithQuantityCategory = headerCellService.findAllByCategory(Category.QUANTITY);

        List<Subcategory> excelHeaderSubcategories = subcategoryService.findAll();

        SubcategoryMapping nameMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithNameCategory, excelHeaderSubcategories);
        SubcategoryMapping priceMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithPriceCategory, excelHeaderSubcategories);
        SubcategoryMapping quantityMapping = ExcelParserHelper.createCategoryMapping(headerCellsWithQuantityCategory, excelHeaderSubcategories);
        ParserFactory parserFactory = new ParserFactory(nameMapping, priceMapping, quantityMapping);

        List<HeaderCellDto> excelHeaderCellsWithSubcategory = ExcelParserHelper.mapToExcelHeaderCellDtos(headerCellService.findAllWithSubcategory());
        ExcelHeaderCellsHandler excelHeaderCellsHandler = new ExcelHeaderCellsHandler(excelHeaderCellsWithSubcategory);

        List<VendorFile> vendorFiles = vendorFileService.findAll(); // find not processed yet

        for (VendorFile vendorFile : vendorFiles) {
            parseExcelFile(vendorFile, excelHeaderCellsHandler, parserFactory);
        }
    }

    private void parseExcelFile(VendorFile vendorFile, ExcelHeaderCellsHandler excelHeaderCellsHandler, ParserFactory parserFactory) {
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
        }

        excelParser.processUnprocessedCells();

        List<Product> foundedProducts = excelParser.parse();

        if (!foundedProducts.isEmpty()) {
            List<ExcelProduct> excelProducts = ExcelParserHelper.mapToExcelProducts(vendorFile, foundedProducts);

            excelProductService.saveAll(excelProducts);

            ExcelStorage excelStorage = new ExcelStorage();
            excelStorage.setVendorFileId(vendorFile.getId());
            excelStorage.setStorages(excelParser.getStorages());
            excelStorageService.save(excelStorage);
        }

        vendorFile.setFileStatus(FileStatus.PARSED);

        vendorFileService.update(vendorFile);
    }

}
