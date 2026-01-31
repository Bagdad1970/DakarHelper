package io.github.bagdad.excelparser.bodyparser;

import io.github.bagdad.excelparser.headerparser.ExcelHeader;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.model.Product;
import io.github.bagdad.excelparser.utils.ExcelCellProcessor;
import io.github.bagdad.models.excelparser.Category;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BodyParser {

    private final Sheet sheet;
    private final ExcelHeader excelHeader;

    public BodyParser(Sheet sheet, ExcelHeader excelHeader) {
        this.sheet = sheet;
        this.excelHeader = excelHeader;
    }

    int getFirstValidRow() {
        int startRowIndex = excelHeader.getStartRowIndex() + 1;
        for (int i = startRowIndex; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (isRowValid(row))
                return i;
        }
        return -1;
    }

    boolean isRowValid(Row row) {
        try {
            Map<Category, Set<Column>> headerColumns = excelHeader.getHeaderColumns();

            if (headerColumns == null || headerColumns.isEmpty()) {
                return false;
            }

            int countCategoryValid = 0;
            for (Category category : headerColumns.keySet()) {
                for (Column column : headerColumns.get(category)) {
                    int columnIndex = column.getColumnIndex();
                    Cell cell = row.getCell(columnIndex);
                    if (ExcelCellProcessor.isCellValid(cell)) {
                        countCategoryValid++;
                        break;
                    }
                }
            }

            return countCategoryValid == headerColumns.size();
        }
        catch (Exception exc) {

        }
        return false;
    }

    public List<Product> parse() {
        List<Product> parsedProducts = new ArrayList<>();

        int startRowIndex = getFirstValidRow();
        for (int i = startRowIndex; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (isRowValid(row)) {
                Product product = excelHeader.processRow(row);

                parsedProducts.add(product);
            }
        }
        return parsedProducts;
    }

}
