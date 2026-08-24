package io.github.bagdad.excelparser.utils;

import io.github.bagdad.excelparser.SheetTest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ExcelCellUtilsTest extends SheetTest {

    @Test
    void Checking_cell_value_for_emptiness_for_blank_value_must_return_true() {
        String blankValue = "   ";

        boolean result = ExcelCellUtils.isCellValueEmpty(blankValue);

        assertThat(result).isTrue();
    }

    @Test
    void Checking_cell_value_for_emptiness_for_value_that_contains_null_string_in_uppercase_must_return_true() {
        String nullValue = "NULL";

        boolean result = ExcelCellUtils.isCellValueEmpty(nullValue);

        assertThat(result).isTrue();
    }

    @Test
    void Checking_cell_value_for_emptiness_for_value_that_contains_null_string_in_lowercase_must_return_true() {
        String nullValue = "null";

        boolean result = ExcelCellUtils.isCellValueEmpty(nullValue);

        assertThat(result).isTrue();
    }

    @Test
    void Checking_cell_value_for_emptiness_for_valid_value_must_return_false() {
        String blankValue = "some_value";

        boolean result = ExcelCellUtils.isCellValueEmpty(blankValue);

        assertThat(result).isFalse();
    }

    @Test
    void Getting_raw_cell_value_for_null_cell_must_return_empty_string() {
        String nullValue = null;
        Sheet sheet = createSheet(nullValue);
        Row row = sheet.getRow(0);

        String nullCellValue = ExcelCellUtils.getRawCellValue(row.getCell(0));

        assertEquals("", nullCellValue);
    }

    @Test
    void Getting_raw_cell_value_for_cell_with_empty_string_must_return_empty_string() {
        String emptyValue = "";
        Sheet sheet = createSheet(emptyValue);
        Row row = CellUtil.getRow(0, sheet);

        String nullCellValue = ExcelCellUtils.getRawCellValue(row.getCell(0));

        assertEquals("", nullCellValue);
    }

    @Test
    void Getting_raw_cell_value_for_numeric_cell_must_return_string_value() {
        Sheet sheet = createSheet(1);
        double cellValue = 12.56;
        sheet.getRow(0).getCell(0).setCellValue(cellValue);

        Row row = CellUtil.getRow(0, sheet);

        String nullCellValue = ExcelCellUtils.getRawCellValue(row.getCell(0));

        assertEquals("12.56", nullCellValue);
    }

}
