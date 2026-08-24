package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.SheetTest;
import io.github.bagdad.excelparser.model.Storage;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.excelparser.utils.ExcelCellUtils;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuantityParserTest extends SheetTest {

//    @BeforeAll
//    static void setupSheet() throws IOException {
//        String[][] quantityHeaderCells = {
//                {"Основной склад", "Склад 2", "Магазин"},
//                {"остаток", "остаток", "остаток"}
//        };
//
//
//        SubcategoryMapping quantityMapping = createQuantitySubcategoryMapping();
//
//        quantityParser = new QuantityParser(quantityMapping);
//    }
//
//    private SubcategoryMapping createQuantitySubcategoryMapping() {
//        Map<String, List<String>> mapping = Map.of(
//                "склад", List.of("склад"),
//                "магазин", List.of("магазин")
//        );
//
//        return new SubcategoryMapping(mapping);
//    }
//
//    @Test
//    void groupCellsBySubcategory() {
//        Map<Integer, List<Cell>> cellsByClass = Map.of(
//                0, List.of(sheet.getRow(0).getCell(0)),
//                1, List.of(sheet.getRow(0).getCell(1)),
//                2, List.of(sheet.getRow(0).getCell(2))
//        );
//
//        Map<String, List<Cell>> res = quantityParser.groupCellsBySubcategory(cellsByClass);
//
//        Map<String, List<Cell>> expected = Map.of(
//                "склад", List.of(sheet.getRow(0).getCell(0), sheet.getRow(0).getCell(1)),
//                "магазин", List.of(sheet.getRow(0).getCell(2))
//        );
//        assertThat(res)
//                .usingRecursiveComparison()
//                .ignoringCollectionOrder()
//                .isEqualTo(expected);
//    }
//
//    @Test
//    void arrangeSubcategoryValuesWithOneCell() {
//        List<Cell> cells = new ArrayList<>();
//        cells.add(sheet.getRow(0).getCell(2));
//
//        Set<Column> res = quantityParser.arrangeSubcategoryValues("магазин", cells);
//
//        Set<Column> expected = Set.of(
//                new QuantityColumn(2, "магазин1", new Storage("магазин1", "магазин 1"))
//        );
//
//        assertEquals(expected, res);
//    }
//
//    @Test
//    void arrangeSubcategoryValuesWithSeveralCells() {
//        List<Cell> cells = new ArrayList<>();
//        cells.add(sheet.getRow(0).getCell(0));
//        cells.add(sheet.getRow(0).getCell(1));
//
//        Set<Column> res = quantityParser.arrangeSubcategoryValues("склад", cells);
//
//        Set<Column> expected = Set.of(
//                new QuantityColumn(0, "склад1", new Storage("склад1", "склад 1")),
//                new QuantityColumn(1, "склад2", new Storage("склад2", "склад 2"))
//        );
//
//        assertEquals(expected, res);
//    }
//
//    @Test
//    void parseColumns() {
//        Map<Integer, List<Cell>> columns = Map.of(
//                0, Arrays.asList(sheet.getRow(0).getCell(0), sheet.getRow(1).getCell(0)),
//                1, Arrays.asList(sheet.getRow(0).getCell(1), sheet.getRow(1).getCell(1)),
//                2, Arrays.asList(sheet.getRow(0).getCell(2), sheet.getRow(1).getCell(2))
//        );
//
//        Set<Column> res = quantityParser.parseColumns(columns);
//
//        Set<Column> expected = new HashSet<>();
//        expected.add(new QuantityColumn(0, "склад1", new Storage("склад1", "склад 1")));
//        expected.add(new QuantityColumn(1, "склад2", new Storage("склад2", "склад 2")));
//        expected.add(new QuantityColumn(2, "магазин1", new Storage("магазин1", "магазин 1")));
//
//        assertEquals(expected, res);
//    }


//    @Test
//    void processQuantityCellWithValue() {
//        Row rowOfQuantityCellValues = CellUtil.getRow(4, sheet);
//
//        Integer processedValue1 = ExcelCellUtils.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 2));
//        Integer processedValue2 = ExcelCellUtils.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 3));
//        Integer processedValue3 = ExcelCellUtils.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 4));
//        Integer processedValue4 = ExcelCellUtils.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 5));
//        Integer processedValue5 = ExcelCellUtils.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 6));
//        Integer processedValue6 = ExcelCellUtils.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 7));
//
//
//        assertEquals(12, processedValue1);
//        assertEquals(12, processedValue2);
//        assertEquals(12, processedValue3);
//        assertEquals(13, processedValue4);
//        assertEquals(0, processedValue5);
//        assertEquals(0, processedValue6);
//    }

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
