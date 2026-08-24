package io.github.bagdad.excelparser;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SheetTest {

    private static Workbook workbook;

    private static Sheet sheet;

    @BeforeEach
    void setUp() throws IOException {
        workbook = WorkbookFactory.create(true);
        sheet = workbook.createSheet();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }

    public static Sheet createSheet(int numberCellsInRow) {
        Row row = sheet.createRow(0);

        for (int i = 0; i < numberCellsInRow; i++) {
            CellUtil.createCell(row, i, "");
        }

        return sheet;
    }

    public static Sheet createSheet(String rowCell) {
        Row row = sheet.createRow(0);

        CellUtil.createCell(row, 0, rowCell);

        return sheet;
    }

    public static Sheet createSheet(String[] rowCells) {
        Row row = sheet.createRow(0);

        for (int i = 0; i < rowCells.length; i++) {
            CellUtil.createCell(row, i, rowCells[i]);
        }

        return sheet;
    }

    public static Sheet createSheet(String[][] rows) {
        for (int i = 0; i < rows.length; i++) {
            Row row = sheet.createRow(i);

            for (int j = 0; j < rows[0].length; j++) {
                CellUtil.createCell(row, j, rows[i][j]);
            }
        }

        return sheet;
    }

    public static Map<Integer, List<Cell>> createColumns(String[] row) {
        if (row == null || row.length == 0) {
            return new HashMap<>();
        }

        Map<Integer, List<Cell>> columns = new HashMap<>();
        for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
            List<Cell> cells = Arrays.asList(sheet.getRow(0).getCell(columnIndex)) ;
            columns.put(columnIndex, cells);
        }

        return columns;
    }

    public static Map<Integer, List<Cell>> createColumns(String[][] rows) {
        if (rows == null || rows.length == 0) {
            return new HashMap<>();
        }

        int columnCount = rows[0].length;
        Map<Integer, List<Cell>> columns = new HashMap<>();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            List<Cell> cells = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
                cells.add(sheet.getRow(rowIndex).getCell(columnIndex));
            }
            columns.put(columnIndex, cells);
        }

        return columns;
    }

}
