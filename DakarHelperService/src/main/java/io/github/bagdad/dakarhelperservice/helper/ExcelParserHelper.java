package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.*;
import io.github.bagdad.excelparser.model.Product;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.ExcelHeaderCellDto;

import java.util.*;

public class ExcelParserHelper {

    public static List<ExcelProduct> mapToExcelProducts(VendorFile vendorFile, List<Product> products) {
        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream().map(product -> {
            ExcelProduct excelProduct = new ExcelProduct();

            excelProduct.setVendorFileId(vendorFile.getId());
            excelProduct.setNames(product.getNames());
            excelProduct.setPrices(product.getPrices());
            excelProduct.setQuantities(product.getQuantities());

            return excelProduct;
        }).toList();
    }

    public static List<ExcelHeaderCell> mapToExcelHeaderCells(List<ExcelHeaderCellDto> excelHeaderCellDtos) {
        if (excelHeaderCellDtos.isEmpty()) {
            return Collections.emptyList();
        }

        return excelHeaderCellDtos.stream()
                .map(unprocessableHeaderCellString -> {
                    ExcelHeaderCell unprocessableColumn = new ExcelHeaderCell();
                    unprocessableColumn.setOriginName(unprocessableHeaderCellString.getOriginName());
                    return unprocessableColumn;
                }).toList();
    }

    public static List<ExcelHeaderCellDto> mapToExcelHeaderCellDtos(List<ExcelHeaderCellWithSubcategory> excelHeaderCellWithSubcategories) {
        if (excelHeaderCellWithSubcategories.isEmpty()) {
            return Collections.emptyList();
        }

        return excelHeaderCellWithSubcategories.stream()
                .map(excelHeaderCellWithSubcategory -> {
                    ExcelHeaderCellDto dto = new ExcelHeaderCellDto();
                    dto.setOriginName(excelHeaderCellWithSubcategory.getOriginName());
                    dto.setCategory(excelHeaderCellWithSubcategory.getCategory());
                    dto.setSubcategoryName(excelHeaderCellWithSubcategory.getSubcategoryName());
                    dto.setNormalizedName(excelHeaderCellWithSubcategory.getNormalizedName());
                    dto.setCellStatus(excelHeaderCellWithSubcategory.getCellStatus());
                    return dto;
                }).toList();
    }

    public static SubcategoryMapping createCategoryMapping(List<ExcelHeaderCell> excelHeaderCells, List<ExcelHeaderSubcategory> excelHeaderSubcategories) {
        Map<String, List<String>> subcategoryMapping = new HashMap<>();
        for (ExcelHeaderSubcategory subcategory : excelHeaderSubcategories) {
            Long subcategoryId = subcategory.getId();

            for (ExcelHeaderCell excelHeaderCell : excelHeaderCells) {
                if (excelHeaderCell.getExcelHeaderSubcategoryId().equals(subcategoryId)) {
                    subcategoryMapping.computeIfAbsent(subcategory.getSubcategoryName(), excelHeaderCell1 -> new ArrayList<>()).add(excelHeaderCell.getOriginName());
                }
            }
        }

        return new SubcategoryMapping(subcategoryMapping);
    }

}
