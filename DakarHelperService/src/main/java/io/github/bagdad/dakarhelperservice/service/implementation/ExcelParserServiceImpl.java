package io.github.bagdad.dakarhelperservice.service.implementation;

import io.github.bagdad.dakarhelperservice.helper.ExcelParserHelper;
import io.github.bagdad.dakarhelperservice.model.*;
import io.github.bagdad.dakarhelperservice.service.interfaces.*;
import io.github.bagdad.excelparser.ExcelParser;
import io.github.bagdad.excelparser.model.Product;
import io.github.bagdad.excelparser.headerparser.ParserFactory;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.ExcelHeaderCellDto;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelParserServiceImpl implements ExcelParserService {

    private final VendorFileService vendorFileService;
    private final ExcelHeaderCellService excelHeaderCellService;
    private final ExcelProductService excelProductService;
    private final ExcelStorageService excelStorageService;
    private final ExcelHeaderSubcategoryService excelHeaderSubcategoryService;

    public ExcelParserServiceImpl(VendorFileService vendorFileService, ExcelHeaderCellService excelHeaderCellService, ExcelProductService excelProductService, ExcelStorageService excelStorageService, ExcelHeaderSubcategoryService excelHeaderSubcategoryService) {
        this.vendorFileService = vendorFileService;
        this.excelHeaderCellService = excelHeaderCellService;
        this.excelProductService = excelProductService;
        this.excelStorageService = excelStorageService;
        this.excelHeaderSubcategoryService = excelHeaderSubcategoryService;
    }

    public void parseFiles() {
        List<ExcelHeaderCell> excelHeaderCellsWithNameCategory = excelHeaderCellService.findAllByCategory(Category.NAME);
        List<ExcelHeaderCell> excelHeaderCellsWithPriceCategory = excelHeaderCellService.findAllByCategory(Category.PRICE);
        List<ExcelHeaderCell> excelHeaderCellsWithQuantityCategory = excelHeaderCellService.findAllByCategory(Category.QUANTITY);

        List<ExcelHeaderSubcategory> excelHeaderSubcategories = excelHeaderSubcategoryService.findAll();

        SubcategoryMapping nameMapping = ExcelParserHelper.createCategoryMapping(excelHeaderCellsWithNameCategory, excelHeaderSubcategories);
        SubcategoryMapping priceMapping = ExcelParserHelper.createCategoryMapping(excelHeaderCellsWithPriceCategory, excelHeaderSubcategories);
        SubcategoryMapping quantityMapping = ExcelParserHelper.createCategoryMapping(excelHeaderCellsWithQuantityCategory, excelHeaderSubcategories);
        ParserFactory parserFactory = new ParserFactory(nameMapping, priceMapping, quantityMapping);

        List<ExcelHeaderCellDto> excelHeaderCellsWithSubcategory = ExcelParserHelper.mapToExcelHeaderCellDtos(excelHeaderCellService.findAllWithSubcategory());
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
            List<ExcelHeaderCellDto> unprocessableHeaderCellDtos = excelParser.getUnprocessableHeaderCells();
            if (!unprocessableHeaderCellDtos.isEmpty()) {
                List<ExcelHeaderCell> unprocessableExcelHeaderCells = ExcelParserHelper.mapToExcelHeaderCells(unprocessableHeaderCellDtos);

                excelHeaderCellService.batchInsert(unprocessableExcelHeaderCells);

                // send to a user

                // get answers by user and update the DB

                // get new headerCells and set them into the ExcelHeaderCells table
            }
        }

        excelParser.processUnprocessedCells();

        List<Product> foundedProducts = excelParser.parse();

        if (!foundedProducts.isEmpty()) {
            List<io.github.bagdad.dakarhelperservice.model.ExcelProduct> excelProducts = ExcelParserHelper.mapToExcelProducts(vendorFile, foundedProducts);

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
