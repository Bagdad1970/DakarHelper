package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.SheetTest;
import io.github.bagdad.excelparser.model.Storage;
import io.github.bagdad.excelparser.headerparser.columns.CategoryColumn;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuantityParserTest extends SheetTest {
    
    private static SubcategoryMapping quantityMapping;

    @BeforeAll
    static void setup() {
        Map<String, List<String>> mapping = Map.of(
                "storage", List.of("storage"),
                "shop", List.of("shop")
        );
        quantityMapping = new SubcategoryMapping(mapping);
    }

    @Test
    void Grouping_valid_cells_by_subcategory_must_return_groups_by_subcategory() {
        String[][] headerCells = {
                {"Main storage", "Storage 2", "Shop"},
                {"count",        "count",     "count"}
        };
        Sheet sheet = createSheet(headerCells);
        Map<Integer, List<Cell>> cellsByClass = Map.of(
                0, List.of(sheet.getRow(0).getCell(0)),
                1, List.of(sheet.getRow(0).getCell(1)),
                2, List.of(sheet.getRow(0).getCell(2))
        );

        QuantityParser sut = new QuantityParser(quantityMapping);
        Map<String, List<Cell>> result = sut.groupCellsBySubcategory(cellsByClass);

        Map<String, List<Cell>> expected = Map.of(
                "storage", List.of(sheet.getRow(0).getCell(0), sheet.getRow(0).getCell(1)),
                "shop", List.of(sheet.getRow(0).getCell(2))
        );
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void Arranging_valid_subcategory_values_only_with_one_cell_must_return_valid_column() {
        String[][] headerCells = {
                {"Shop"},
                {"count"}
        };
        Sheet sheet = createSheet(headerCells);

        List<Cell> cells = new ArrayList<>();
        cells.add(sheet.getRow(0).getCell(0));

        QuantityParser sut = new QuantityParser(quantityMapping);
        Set<CategoryColumn> result = sut.arrangeSubcategoriesByNumber("shop", cells);

        Set<CategoryColumn> expected = Set.of(
                new QuantityColumn(0, "shop1", new Storage("shop1", "shop 1"))
        );

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void Arranging_valid_subcategory_values_with_several_cells_must_return_valid_columns() {
        String[][] headerCells = {
                {"Main storage", "Storage 2"},
                {"count",        "count"}
        };
        Sheet sheet = createSheet(headerCells);

        List<Cell> cells = new ArrayList<>();
        cells.add(sheet.getRow(0).getCell(0));
        cells.add(sheet.getRow(0).getCell(1));

        QuantityParser sut = new QuantityParser(quantityMapping);
        Set<CategoryColumn> result = sut.arrangeSubcategoriesByNumber("storage", cells);

        Set<CategoryColumn> expected = Set.of(
                new QuantityColumn(0, "storage1", new Storage("storage1", "storage 1")),
                new QuantityColumn(1, "storage2", new Storage("storage2", "storage 2"))
        );

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void parseColumns() {
        String[][] headerCells = {
                {"Main storage", "Storage 2", "Shop"},
                {"count",        "count",     "count"}
        };
        Sheet sheet = createSheet(headerCells);

        Map<Integer, List<Cell>> columns = Map.of(
                0, Arrays.asList(sheet.getRow(0).getCell(0), sheet.getRow(1).getCell(0)),
                1, Arrays.asList(sheet.getRow(0).getCell(1), sheet.getRow(1).getCell(1)),
                2, Arrays.asList(sheet.getRow(0).getCell(2), sheet.getRow(1).getCell(2))
        );

        QuantityParser sut = new QuantityParser(quantityMapping);
        Set<CategoryColumn> result= sut.parseColumns(columns);

        Set<CategoryColumn> expected = new HashSet<>();
        expected.add(new QuantityColumn(0, "storage1", new Storage("storage1", "storage 1")));
        expected.add(new QuantityColumn(1, "storage2", new Storage("storage2", "storage 2")));
        expected.add(new QuantityColumn(2, "shop1", new Storage("shop1", "shop 1")));

        assertThat(result).isEqualTo(expected);
    }


    @Test
    void Processing_null_cell_must_return_null() {
        String value = null;
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_empty_cell_must_return_null() {
        String value = "";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_empty_cell_with_whitespaces_must_return_null() {
        String value = "    ";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_integer_cell_value_must_return_integer() {
        String value = "12";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isEqualTo(12);
    }

    @Test
    void Processing_double_cell_value_must_return_integer() {
        String value = "12.56";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isEqualTo(12);
    }

    @Test
    void Processing_double_cell_value_with_whitespaces_must_return_integer() {
        String value = "   12.56   ";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isEqualTo(12);
    }

    @Test
    void Processing_cell_value_with_more_as_symbol_must_return_integer_plus_one() {
        String value = "  >12  ";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isEqualTo(13);
    }

    @Test
    void Processing_cell_value_with_more_as_string_must_return_integer_plus_one() {
        String value = "более 12";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isEqualTo(13);
    }

    @Test
    void Processing_zero_cell_value_must_return_zero() {
        String value = "0";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isZero();
    }

    @Test
    void Processing_minus_one_cell_value_must_return_minus_one() {
        String value = "-1";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        Integer result = QuantityParser.processCell(cell);

        assertThat(result).isEqualTo(0);
    }

}
