package io.github.bagdad.excelparser.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExcelWorkbookReaderTest {

    private static final String FILES_DIR = "/excel-files/";

    private ExcelWorkbookReader excelReader;

    @AfterEach
    void closeWorkbook() {
        excelReader.close();
    }

    @Test
    void Loading_workbook_of_valid_xls_file_must_return_valid_workbook() {
        Path filepath = Paths.get(getClass().getResource(FILES_DIR + "valid.xls").getPath());

        excelReader = new ExcelWorkbookReader(filepath);

        assertThat(excelReader.getWorkbook()).isInstanceOf(HSSFWorkbook.class);
    }

    @Test
    void Loading_workbook_of_valid_xlsx_file_must_return_valid_workbook() {
        Path filepath = Paths.get(getClass().getResource(FILES_DIR + "valid.xlsx").getPath());

        excelReader = new ExcelWorkbookReader(filepath);

        assertThat(excelReader.getWorkbook()).isInstanceOf(XSSFWorkbook.class);
    }

    @Test
    void Loading_workbook_of_file_with_invalid_format_must_return_null() {
        Path filepath = Paths.get(getClass().getResource(FILES_DIR + "invalid-format.xxx").getPath());

        excelReader = new ExcelWorkbookReader(filepath);

        assertThat(excelReader.getWorkbook()).isNull();
    }

    @Test
    void Loading_workbook_of_nonexisting_file_must_return_null() {
        Path filepath = Path.of("");

        excelReader = new ExcelWorkbookReader(filepath);

        assertThat(excelReader.getWorkbook()).isNull();
    }

    @Test
    void Getting_first_sheet_of_valid_workbook_must_return_first_sheet() {
        Path filepath = Paths.get(getClass().getResource(FILES_DIR + "with-sheets.xls").getPath());
        excelReader = new ExcelWorkbookReader(filepath);

        Sheet sheet = excelReader.getFirstSheet();

        assertThat(sheet).isNotNull();
    }

    @Test
    void Getting_first_sheet_of_empty_workbook_must_return_null() {
        Path filepath = Paths.get(getClass().getResource(FILES_DIR + "without-sheets.xls").getPath());
        excelReader = new ExcelWorkbookReader(filepath);

        Sheet sheet = excelReader.getFirstSheet();

        assertThat(sheet).isNull();
    }

}
