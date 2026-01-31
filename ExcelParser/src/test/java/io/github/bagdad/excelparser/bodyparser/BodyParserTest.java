package io.github.bagdad.excelparser.bodyparser;

import io.github.bagdad.excelparser.headerparser.ExcelHeader;
import io.github.bagdad.excelparser.headerparser.Storage;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.NameColumn;
import io.github.bagdad.excelparser.headerparser.columns.PriceColumn;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.models.excelparser.Category;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BodyParserTest {

    private static Workbook workbook;
    private static Sheet sheet;
    private static BodyParser sut;

    @BeforeAll
    static void setupSheet() throws IOException {
        workbook = WorkbookFactory.create(true);
        sheet = workbook.createSheet();

        String[][] cells = {
                {"",      "",           "",        ""},
                {"name1", "wholesale1", "",        "count1"},
                {"name2", "wholesale2", "retail2", "count2"},
        };

        for (int i = 0; i < cells.length; i++) {
            Row currentRow = sheet.createRow(i);
            for (int j = 0; j < cells[i].length; j++) {
                CellUtil.createCell(currentRow, j, cells[i][j]);
            }
        }

        Map<Category, Set<Column>> headerColumns = Map.of(
                Category.NAME, Set.of(new NameColumn(0, "name")),
                Category.PRICE, Set.of(
                        new PriceColumn(1, "wholesale"),
                        new PriceColumn(2, "retail")
                ),
                Category.QUANTITY, Set.of(new QuantityColumn(3, "count",
                        new Storage("storage1", "storage 1")))
        );
        ExcelHeader excelHeader = new ExcelHeader(1, headerColumns);
        sut = new BodyParser(sheet, excelHeader);
    }

    @Test
    void Validation_with_an_empty_row_is_false() {
        Row row = sheet.getRow(0);

        boolean result = sut.isRowValid(row);

        assertFalse(result);
    }

    @Test
    void Validation_with_a_not_full_row_is_true() {
        Row row = sheet.getRow(1);

        boolean result = sut.isRowValid(row);

        assertTrue(result);
    }

    @Test
    void Validation_with_a_full_row_is_true() {
        Row row = sheet.getRow(2);

        boolean result = sut.isRowValid(row);

        assertTrue(result);
    }

}
