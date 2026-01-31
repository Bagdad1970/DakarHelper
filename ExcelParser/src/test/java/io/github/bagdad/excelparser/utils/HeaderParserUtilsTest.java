package io.github.bagdad.excelparser.utils;

import io.github.bagdad.models.excelparser.Category;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeaderParserUtilsTest {

    private static Workbook workbook;
    private static Sheet sheet;

    @BeforeAll
    static void setupSheet() throws IOException {
        workbook = WorkbookFactory.create(true);
        sheet = workbook.createSheet("Test Sheet");

        for (int i=0; i<6; i++) {
            sheet.createRow(i);
        }

        CellUtil.createCell(sheet.getRow(0), 0, "номенклатура");
        CellUtil.createCell(sheet.getRow(1), 1, "номенклатура");

        CellUtil.createCell(sheet.getRow(0), 2, "цена");
        CellUtil.createCell(sheet.getRow(0), 3, "цена");
        CellUtil.createCell(sheet.getRow(1), 3, "оптовый");

        CellUtil.createCell(sheet.getRow(0), 4, "количество");
        CellUtil.createCell(sheet.getRow(1), 4, "остаток");
    }

    @Test
    void findMaxRowIndexInGroups() {
        Map<Category, List<Cell>> input = Map.of(
                Category.NAME, List.of(
                        sheet.getRow(0).getCell(0),
                        sheet.getRow(1).getCell(1)
                ),
                Category.PRICE, List.of(
                        sheet.getRow(0).getCell(2),
                        sheet.getRow(0).getCell(3),
                        sheet.getRow(1).getCell(3)
                ),
                Category.QUANTITY, List.of(
                        sheet.getRow(0).getCell(4),
                        sheet.getRow(1).getCell(4)
                )
        );

        Map<Category, Integer> result = HeaderParserUtils.findMaxRowIndexInGroups(input);

        Map<Category, Integer> expected = Map.of(
                Category.NAME, 1,
                Category.PRICE, 1,
                Category.QUANTITY, 1
        );

        assertEquals(expected, result);
    }

    @Test
    void findMaxRowIndexInCellsWithEmptyInput() {
        int result = HeaderParserUtils.findMaxRowIndexInCells(new ArrayList<>());

        assertEquals(-1, result);
    }

    @Test
    void findMaxRowIndexInCells() {
        List<Cell> input = List.of(
                sheet.getRow(0).getCell(0),
                sheet.getRow(1).getCell(1)
        );

        int result = HeaderParserUtils.findMaxRowIndexInCells(input);

        int expected = 1;

        assertEquals(expected, result);
    }

    @Test
    void groupByColumn() {
        Map<Category, List<Cell>> input = Map.of(
                Category.NAME, List.of(
                        sheet.getRow(0).getCell(0),
                        sheet.getRow(1).getCell(1)
                ),
                Category.PRICE, List.of(
                        sheet.getRow(0).getCell(2),
                        sheet.getRow(0).getCell(3),
                        sheet.getRow(1).getCell(3)
                ),
                Category.QUANTITY, List.of(
                        sheet.getRow(0).getCell(4),
                        sheet.getRow(1).getCell(4)
                )
        );

        Map<Category, Map<Integer, List<Cell>>> groupedCellsByColumn = HeaderParserUtils.groupByColumn(input);

        Map<Category, Map<Integer, List<Cell>>> expected = Map.of(
            Category.NAME, Map.of(
                        0, List.of(sheet.getRow(0).getCell(0)),
                        1, List.of(sheet.getRow(1).getCell(1))),
            Category.PRICE, Map.of(
                        2, List.of(sheet.getRow(0).getCell(2)),
                        3, List.of(sheet.getRow(0).getCell(3), sheet.getRow(1).getCell(3))),
            Category.QUANTITY, Map.of(
                        4, List.of(sheet.getRow(0).getCell(4), sheet.getRow(1).getCell(4)))
        );

        assertEquals(expected, groupedCellsByColumn);
    }

    @Test
    void removeGroupsWithoutMaxRow() {
        Map<Category, Map<Integer, List<Cell>>> input = Map.of(
                Category.NAME, Map.of(
                        0, List.of(sheet.getRow(0).getCell(0)),
                        1, List.of(sheet.getRow(1).getCell(1))),
                Category.PRICE, Map.of(
                        2, List.of(sheet.getRow(0).getCell(2)),
                        3, List.of(sheet.getRow(0).getCell(3), sheet.getRow(1).getCell(3))),
                Category.QUANTITY, Map.of(
                        4, List.of(sheet.getRow(0).getCell(4), sheet.getRow(1).getCell(4)))
        );

        Map<Category, Integer> maxRowIndexForCategories = Map.of(
                Category.NAME, 1,
                Category.PRICE, 1,
                Category.QUANTITY, 1
        );

        Map<Category, Map<Integer, List<Cell>>> result = HeaderParserUtils.removeGroupsWithoutMaxRow(maxRowIndexForCategories, input);

        Map<Category, Map<Integer, List<Cell>>> expected = Map.of(
                Category.NAME, Map.of(
                        1, List.of(sheet.getRow(1).getCell(1))),
                Category.PRICE, Map.of(
                        3, List.of(sheet.getRow(0).getCell(3), sheet.getRow(1).getCell(3))),
                Category.QUANTITY, Map.of(
                        4, List.of(sheet.getRow(0).getCell(4), sheet.getRow(1).getCell(4)))
        );

        assertEquals(expected, result);
    }


}
