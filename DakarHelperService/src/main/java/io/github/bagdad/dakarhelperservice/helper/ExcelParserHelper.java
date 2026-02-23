package io.github.bagdad.dakarhelperservice.helper;

import io.github.bagdad.dakarhelperservice.model.HeaderCell;
import io.github.bagdad.dakarhelperservice.model.HeaderCellWithSubcategory;
import io.github.bagdad.dakarhelperservice.model.Product;
import io.github.bagdad.dakarhelperservice.model.Subcategory;
import io.github.bagdad.excelparser.headerparser.model.ExcelProduct;
import io.github.bagdad.excelparser.headerparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.HeaderCellDto;

import java.util.*;

public class ExcelParserHelper {

    public static List<Product> mapToProducts(Long vendorId, List<ExcelProduct> excelProducts) {
        if (excelProducts.isEmpty()) {
            return Collections.emptyList();
        }

        return excelProducts.stream().map(excelProduct -> Product.builder()
                .vendorId(vendorId)
                .name(excelProduct.getName())
                .prices(excelProduct.getPrices())
                .minPrice(excelProduct.getMinPrice())
                .quantities(excelProduct.getQuantities())
                .totalQuantity(excelProduct.getTotalQuantity())
                .build()).toList();
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
