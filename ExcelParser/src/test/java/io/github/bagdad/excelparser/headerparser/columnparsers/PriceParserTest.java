package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.SheetTest;
import io.github.bagdad.excelparser.headerparser.columns.CategoryColumn;
import io.github.bagdad.excelparser.headerparser.columns.PriceColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PriceParserTest extends SheetTest {

    @Test
    void parseColumns() {
        String[][] cells = {
                {"оптовая", "розничная", "интернет"},
                {"цена", "цена", "цена"}
        };
        createSheet(cells);

        Map<String, List<String>> mapping = java.util.Map.of(
                "опт", List.of("оптовая"),
                "розница", List.of("розничная"),
                "интернет", List.of("интернет")
        );
        SubcategoryMapping priceMapping = new SubcategoryMapping(mapping);
        Map<Integer, List<Cell>> columns = createColumns(cells);

        PriceParser sut = new PriceParser(priceMapping);
        Set<CategoryColumn> result = sut.parseColumns(columns);

        Set<CategoryColumn> expected = Set.of(
            new PriceColumn(0, "опт"),
            new PriceColumn(1, "розница"),
            new PriceColumn(2, "интернет")
        );

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void Processing_null_cell_must_return_null() {
        String value = null;
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        BigDecimal result = PriceParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_empty_cell_must_return_null() {
        String value = "";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        BigDecimal result = PriceParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_empty_cell_with_whitespaces_must_return_null() {
        String value = "    ";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        BigDecimal result = PriceParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_integer_cell_value_must_return_big_decimal_with_its_integer() {
        String value = "12";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        BigDecimal result = PriceParser.processCell(cell);

        assertThat(result).isEqualTo(BigDecimal.valueOf(12.0));
    }

    @Test
    void Processing_double_cell_value_must_return_big_decimal_with_its_double() {
        String value = "12.56";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        BigDecimal result = PriceParser.processCell(cell);

        assertThat(result).isEqualTo(BigDecimal.valueOf(12.56));
    }

    @Test
    void Processing_double_cell_value_with_whitespaces_must_return_big_decimal_with_its_double() {
        String value = "   12.56   ";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        BigDecimal result = PriceParser.processCell(cell);

        assertThat(result).isEqualTo(BigDecimal.valueOf(12.56));
    }

    @Test
    void Processing_zero_cell_value_must_return_null() {
        String value = "0";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        BigDecimal result = PriceParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_negative_cell_value_must_return_null() {
        String value = "-1";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        BigDecimal result = PriceParser.processCell(cell);

        assertThat(result).isNull();
    }

}
