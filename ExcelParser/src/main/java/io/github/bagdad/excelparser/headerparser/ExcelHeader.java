package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.headerparser.columnparsers.ArticleParser;
import io.github.bagdad.excelparser.headerparser.columnparsers.NameParser;
import io.github.bagdad.excelparser.headerparser.columnparsers.PriceParser;
import io.github.bagdad.excelparser.headerparser.columnparsers.QuantityParser;
import io.github.bagdad.excelparser.model.ExcelProduct;
import io.github.bagdad.excelparser.headerparser.columns.CategoryColumn;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.models.excelparser.Category;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class ExcelHeader {

    private final Map<Category, Set<CategoryColumn>> headerColumns;

    private final int startRowIndex;

    public ExcelHeader(int startRowIndex) {
        this.startRowIndex = startRowIndex;
        this.headerColumns = new HashMap<>();
    }

    public ExcelHeader(int startRowIndex, Map<Category, Set<CategoryColumn>> headerColumns) {
        this.startRowIndex = startRowIndex;
        this.headerColumns = headerColumns;
    }

    public ExcelProduct processRow(Row row) {
        ExcelProduct excelProduct = new ExcelProduct();

        for (Category category : headerColumns.keySet()) {
            Set<CategoryColumn> columnsByCategory = headerColumns.get(category);

            if (columnsByCategory == null || columnsByCategory.isEmpty()) {
                return null;
            }

            for (CategoryColumn column : columnsByCategory) {
                Cell cell = row.getCell(column.getColumnIndex());

                if (category == Category.ARTICLE) {
                    String value = ArticleParser.processCell(cell);
                    if (value != null) {
                        excelProduct.setArticle(value);
                    }
                }
                else if (category == Category.NAME) {
                    String value = NameParser.processCell(cell);
                    if (value != null) {
                        excelProduct.setName(value);
                    }
                }
                else if (category == Category.PRICE) {
                    BigDecimal value = PriceParser.processCell(cell);
                    if (value != null) {
                        excelProduct.addPrice(column.getColumnKey(), value);
                    }
                }
                else if (category == Category.QUANTITY) {
                    Integer value = QuantityParser.processCell(cell);
                    if (value != null) {
                        excelProduct.addQuantity(column.getColumnKey(), value);
                    }
                }
            }
        }

        return excelProduct;
    }

    public Map<String, String> getStorages() {
        return headerColumns.get(Category.QUANTITY).stream()
                .map(QuantityColumn.class::cast)
                .collect(Collectors.toMap(
                        column -> column.getStorage().getKey(),
                        column -> column.getStorage().getName(),
                        (a, b) -> a
                ));
    }

    public void putAllHeaderColumns(Category category, Set<CategoryColumn> headerColumnsByCategory) {
        headerColumns.computeIfAbsent(category, _ -> new HashSet<>()).addAll(headerColumnsByCategory);
    }

}
