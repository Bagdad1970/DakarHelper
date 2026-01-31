package io.github.bagdad.excelparser.headerparser;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class HeaderExtractorTest {

    private static Workbook workbook;

    @BeforeAll
    static void setupSheet() throws IOException {
        workbook = WorkbookFactory.create(true);
        Sheet sheet = workbook.createSheet();

        String[][] headerCells = {
                {"номенклатура", "",        "опт", "розница", "Склад 1", "Склад 2"},
                {"артикул", "номенклатура", "цена", "цена",   "остаток", "остаток"},
                {"0000000", "LDF 45/7 12.3", "123.45", "123.89",   "13", "15"}
        };

        for (int i=0; i<headerCells.length; i++) {
            Row row = sheet.createRow(i);
            for (int j=0; j<headerCells[0].length; j++) {
                CellUtil.createCell(row, j, headerCells[row.getRowNum()][j]);
            }
        }
    }

    @Test
    void Extracted_header_must_contain_all_rows_before_the_first_row_with_two_numeric_values() {
        Sheet sheet = workbook.getSheetAt(0);
        HeaderExtractor extractor = new HeaderExtractor(sheet);

        List<Row> result = extractor.extractHeader();

        List<Row> expected = List.of(
                sheet.getRow(0),
                sheet.getRow(1)
        );

        Assertions.assertEquals(expected, result);
    }

}
