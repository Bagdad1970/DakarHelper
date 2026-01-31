package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.Storage;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuantityParserTest {

    private static Workbook workbook;
    private static Sheet sheet;
    private static QuantityParser quantityParser;

    @BeforeAll
    static void setupSheet() throws IOException {
        workbook = WorkbookFactory.create(true);
        sheet = workbook.createSheet();

        String[][] quantityHeaderCells = {
                {"Основной склад", "Склад 2", "Магазин"},
                {"остаток", "остаток", "остаток"}
        };

        for (int i=0; i<quantityHeaderCells.length; i++) {
            Row row = sheet.createRow(i);
            for (int j=0; j<quantityHeaderCells[0].length; j++) {
                CellUtil.createCell(row, j, quantityHeaderCells[i][j]);
            }
        }

        Map<String, List<String>> mapping = Map.of(
                "склад", List.of("склад"),
                "магазин", List.of("магазин")
        );
        SubcategoryMapping quantityMapping = new SubcategoryMapping(mapping);

        quantityParser = new QuantityParser(quantityMapping);
    }

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
//                "магаз", List.of(sheet.getRow(0).getCell(2))
//        );
//
//        assertEquals(expected, res);
//    }

    @Test
    void arrangeSubcategoryValuesWithOneCell() {
        List<Cell> cells = new ArrayList<>();
        cells.add(sheet.getRow(0).getCell(2));

        Set<Column> res = quantityParser.arrangeSubcategoryValues("магазин", cells);

        Set<Column> expected = Set.of(
                new QuantityColumn(1, "магазин1", new Storage("магазин1", "магазин 1"))
        );

        assertEquals(expected, res);
    }

    @Test
    void arrangeSubcategoryValuesWithSeveralCells() {
        List<Cell> cells = new ArrayList<>();
        cells.add(sheet.getRow(0).getCell(0));
        cells.add(sheet.getRow(0).getCell(1));

        Set<Column> res = quantityParser.arrangeSubcategoryValues("склад", cells);

        Set<Column> expected = Set.of(
                new QuantityColumn(0, "склад1", new Storage("склад1", "склад 1")),
                new QuantityColumn(1, "склад2", new Storage("склад2", "склад 2"))
        );

        assertEquals(expected, res);
    }

    @Test
    void parseColumns() {
        Map<Integer, List<Cell>> columns = Map.of(
                0, Arrays.asList(sheet.getRow(0).getCell(0), sheet.getRow(1).getCell(0)),
                1, Arrays.asList(sheet.getRow(0).getCell(1), sheet.getRow(1).getCell(1)),
                2, Arrays.asList(sheet.getRow(0).getCell(2), sheet.getRow(1).getCell(2))
        );

        Set<Column> res = quantityParser.parseColumns(columns);

        Set<Column> expected = new HashSet<>();
        expected.add(new QuantityColumn(0, "склад1", new Storage("склад1", "склад 1")));
        expected.add(new QuantityColumn(1, "склад2", new Storage("склад2", "склад 2")));
        expected.add(new QuantityColumn(2, "магазин1", new Storage("магазин1", "магазин 1")));

        assertEquals(expected, res);
    }

}
