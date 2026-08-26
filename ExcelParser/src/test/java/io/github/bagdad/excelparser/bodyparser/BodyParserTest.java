package io.github.bagdad.excelparser.bodyparser;

import io.github.bagdad.excelparser.SheetTest;
import io.github.bagdad.excelparser.headerparser.ExcelHeader;
import io.github.bagdad.excelparser.model.Storage;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.NameColumn;
import io.github.bagdad.excelparser.headerparser.columns.PriceColumn;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.models.excelparser.Category;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class BodyParserTest extends SheetTest {

    private static ExcelHeader excelHeader;

    @BeforeAll
    static void setup() {
        Map<Category, Set<Column>> headerColumns = Map.of(
                Category.NAME, Set.of(new NameColumn(0, "name")),
                Category.PRICE, Set.of(
                        new PriceColumn(1, "wholesale"),
                        new PriceColumn(2, "retail")
                ),
                Category.QUANTITY, Set.of(new QuantityColumn(3, "count",
                        new Storage("storage1", "storage 1")))
        );

        excelHeader = new ExcelHeader(1, headerColumns);
    }

    @Test
    void Validation_with_an_empty_row_is_false() {
        String[] emptyRow = {"", "", "", ""};
        Sheet sheet = createSheet(emptyRow);
        Row row = sheet.getRow(0);

        BodyParser sut = new BodyParser(sheet, excelHeader);
        boolean result = sut.isRowValid(row);

        assertFalse(result);
    }

    @Test
    void Validation_with_not_full_row_is_true() {
        String[] notFullRow = {"name", "wholesale", "", "count"};
        Sheet sheet = createSheet(notFullRow);
        Row row = sheet.getRow(0);

        BodyParser sut = new BodyParser(sheet, excelHeader);
        boolean result = sut.isRowValid(row);

        assertTrue(result);
    }

    @Test
    void Validation_with_a_full_row_is_true() {
        String[] fullRow = {"name", "wholesale", "retail", "count"};
        Sheet sheet = createSheet(fullRow);
        Row row = sheet.getRow(0);

        BodyParser sut = new BodyParser(sheet, excelHeader);
        boolean result = sut.isRowValid(row);

        assertTrue(result);
    }

    @Test
    void Getting_first_valid_row_for_sheet_with_header_and_body_must_return_first_body_row_index() {
        String[][] cells = {
                {"name", "wholesale", "retail", "count"},
                {"some_name", "123.45", "134.56", "10"}
        };
        Sheet sheet = createSheet(cells);

        BodyParser sut = new BodyParser(sheet, excelHeader);
        int result = sut.getFirstValidRow();

        assertThat(result).isEqualTo(1);
    }

    @Test
    void Getting_first_valid_row_for_sheet_with_header_and_empty_body_must_return_minus_one() {
        String[][] cells = {
                {"name", "wholesale", "retail", "count"},
                {"", "", "", ""}
        };
        Sheet sheet = createSheet(cells);

        BodyParser sut = new BodyParser(sheet, excelHeader);
        int result = sut.getFirstValidRow();

        assertThat(result).isEqualTo(-1);
    }

}
