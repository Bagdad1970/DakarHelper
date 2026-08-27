package io.github.bagdad.excelparser.bodyparser;

import io.github.bagdad.excelparser.headerparser.ExcelHeader;
import io.github.bagdad.excelparser.headerparser.columns.CategoryColumn;
import io.github.bagdad.excelparser.model.ExcelProduct;
import io.github.bagdad.excelparser.utils.ExcelCellUtils;
import io.github.bagdad.models.excelparser.Category;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class BodyParser {

    private final Sheet sheet;

    private final ExcelHeader excelHeader;

    public BodyParser(Sheet sheet, ExcelHeader excelHeader) {
        this.sheet = sheet;
        this.excelHeader = excelHeader;
    }

    int getFirstValidRow() {
        log.info("Getting index of first valid row");

        int startRowIndex = excelHeader.getStartRowIndex();
        for (int i = startRowIndex; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (isRowValid(row))
                return i;
        }

        return -1;
    }

    boolean isRowValid(Row row) {
        Map<Category, Set<CategoryColumn>> headerColumns = excelHeader.getHeaderColumns();

        if (headerColumns.isEmpty()) {
            return false;
        }

        int providedCategoryCounter = 0;
        for (Category category : headerColumns.keySet()) {
            for (CategoryColumn column : headerColumns.get(category)) {
                int columnIndex = column.getColumnIndex();
                Cell cell = row.getCell(columnIndex);
                if (ExcelCellUtils.isCellValid(cell)) {
                    providedCategoryCounter++;
                    break;
                }
            }
        }

        return providedCategoryCounter == headerColumns.size();
    }

    public List<ExcelProduct> parse() {
        log.info("Parsing table rows");

        int startRowIndex = getFirstValidRow();
        if (startRowIndex == -1)
            return Collections.emptyList();

        List<ExcelProduct> parsedExcelProducts = new ArrayList<>();
        for (int i = startRowIndex; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row == null || isRowValid(row)) {
                continue;
            }

            ExcelProduct excelProduct = excelHeader.processRow(row);
            excelProduct.compute();

            if (!excelProduct.isEmpty()) {
                parsedExcelProducts.add(excelProduct);
            }
        }

        return parsedExcelProducts;
    }

}
