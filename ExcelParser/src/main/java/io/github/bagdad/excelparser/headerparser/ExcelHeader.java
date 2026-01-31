package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.model.Product;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.excelparser.utils.ExcelCellProcessor;
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

    private final Map<Category, Set<Column>> headerColumns;
    private final int startRowIndex;

    public ExcelHeader(int startRowIndex) {
        this.startRowIndex = startRowIndex;
        this.headerColumns = new HashMap<>();
    }

    public ExcelHeader(int startRowIndex, Map<Category, Set<Column>> headerColumns) {
        this.startRowIndex = startRowIndex;
        this.headerColumns = headerColumns;
    }

    public Product processRow(Row row) {
        Product product = new Product();

        for (Category category : headerColumns.keySet()) {
            Set<Column> columnsByCategory = headerColumns.get(category);

            if (columnsByCategory == null || columnsByCategory.isEmpty()) {
                return null;
            }

            for (Column column : columnsByCategory) {
                Cell cell = row.getCell(column.getColumnIndex());

                if (category == Category.NAME) {
                    String value = ExcelCellProcessor.processNameCell(cell);
                    if (value != null) {
                        product.addName(column.getColumnKey(), value);
                    }
                }
                else if (category == Category.PRICE) {
                    BigDecimal value = ExcelCellProcessor.processPriceCell(cell);
                    if (value != null) {
                        product.addPrice(column.getColumnKey(), value);
                    }
                }
                else if (category == Category.QUANTITY) {
                    Integer value = ExcelCellProcessor.processQuantityCell(cell);
                    if (value != null) {
                        product.addQuantity(column.getColumnKey(), value);
                    }
                }
            }
        }

        return product;
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

    public void putAllHeaderColumns(Category category, Set<Column> headerColumnsByCategory) {
        headerColumns.computeIfAbsent(category, _ -> new HashSet<>()).addAll(headerColumnsByCategory);
    }

}
