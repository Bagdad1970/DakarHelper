package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.dakarhelperservice.model.VendorFile;
import io.github.bagdad.excelparser.model.ExcelProduct;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.HeaderCellDto;

import java.util.*;

public class ExcelParserHelper {

    public static List<Product> mapToExcelProducts(VendorFile vendorFile, List<ExcelProduct> excelProducts) {
        if (excelProducts.isEmpty()) {
            return Collections.emptyList();
        }

        return excelProducts.stream().map(product -> {
            Product excelProduct = new Product();

            excelProduct.setVendorFileId(vendorFile.getId());
            excelProduct.setName(product.getName());
            excelProduct.setPrices(product.getPrices());
            excelProduct.setMinPrice(product.getMinPrice());
            excelProduct.setQuantities(product.getQuantities());
            excelProduct.setTotalQuantity(product.getTotalQuantity());

            return excelProduct;
        }).toList();
    }

    public static List<HeaderCell> mapToExcelHeaderCells(List<HeaderCellDto> headerCellDtos) {
        if (headerCellDtos.isEmpty()) {
            return Collections.emptyList();
        }

        return headerCellDtos.stream()
                .map(unprocessableHeaderCellString -> {
                    HeaderCell unprocessableColumn = new HeaderCell();
                    unprocessableColumn.setOriginalName(unprocessableHeaderCellString.getOriginalName());
                    return unprocessableColumn;
                }).toList();
    }

    public static List<HeaderCellDto> mapToExcelHeaderCellDtos(List<HeaderCellWithSubcategory> excelHeaderCellWithSubcategories) {
        if (excelHeaderCellWithSubcategories.isEmpty()) {
            return Collections.emptyList();
        }

        return excelHeaderCellWithSubcategories.stream()
                .map(excelHeaderCellWithSubcategory -> {
                    HeaderCellDto dto = new HeaderCellDto();
                    dto.setOriginalName(excelHeaderCellWithSubcategory.getOriginName());
                    dto.setCategory(excelHeaderCellWithSubcategory.getCategory());
                    dto.setSubcategoryName(excelHeaderCellWithSubcategory.getSubcategoryName());
                    dto.setNormalizedName(excelHeaderCellWithSubcategory.getNormalizedName());
                    dto.setCellStatus(excelHeaderCellWithSubcategory.getCellStatus());
                    return dto;
                }).toList();
    }

    public static SubcategoryMapping createCategoryMapping(List<HeaderCell> headerCells, List<Subcategory> excelHeaderSubcategories) {
        Map<String, List<String>> subcategoryMapping = new HashMap<>();
        for (Subcategory subcategory : excelHeaderSubcategories) {
            Long subcategoryId = subcategory.getId();

            for (HeaderCell headerCell : headerCells) {
                if (headerCell.getSubcategoryId().equals(subcategoryId)) {
                    subcategoryMapping.computeIfAbsent(subcategory.getName(), excelHeaderCell1 -> new ArrayList<>()).add(headerCell.getOriginalName());
                }
            }
        }

        return new SubcategoryMapping(subcategoryMapping);
    }

}
