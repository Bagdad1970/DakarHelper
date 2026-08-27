package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.SheetTest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class HeaderExtractorTest extends SheetTest {

    private static Workbook workbook;

    @Test
    void Extracted_header_must_contain_all_rows_before_the_first_row_with_two_numeric_values() {
        String[][] header = {
                {"номенклатура", "",        "опт", "розница", "Склад 1", "Склад 2"},
                {"артикул", "номенклатура", "цена", "цена",   "остаток", "остаток"},
                {"0000000", "some_name", "123.45", "123.89",   "13", "15"}
        };

        Sheet sheet = createSheet(header);
        HeaderExtractor extractor = new HeaderExtractor(sheet);

        List<Row> result = extractor.extractHeader();

        List<Row> expected = List.of(
                sheet.getRow(0),
                sheet.getRow(1)
        );

        Assertions.assertEquals(expected, result);
    }

}
