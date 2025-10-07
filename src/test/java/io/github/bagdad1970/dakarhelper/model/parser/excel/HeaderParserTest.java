package io.github.bagdad1970.dakarhelper.model.parser.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HeaderParserTest {

    private final File fileWithoutStorage = new File("src/test/resources/io.github.bagdad1970.dakarhelper/model.parser.excel/without-storage.xls");
    private final File fileWithOneStorage = new File("src/test/resources/io.github.bagdad1970.dakarhelper/model.parser.excel/with-one-storage.xls");
    private final File fileWithSeveralSimpleStorages = new File("src/test/resources/io.github.bagdad1970.dakarhelper/model.parser.excel/with-several-simple-storages.xls");
    private final File fileWithSeveralComplexStorages = new File("src/test/resources/io.github.bagdad1970.dakarhelper/model.parser.excel/with-several-complex-storage.xls");
    private final File fileForFindingHeaderCells = new File("src/test/resources/io.github.bagdad1970.dakarhelper/model.parser.excel/for_header_parser.xls");

    private static Sheet getSheet(File file) {
        try (FileInputStream excelFileStream = new FileInputStream(file)) {
            Workbook workbook;
            if (file.getName().endsWith(".xls")) {
                workbook = new HSSFWorkbook(excelFileStream);
            }
            else if (file.getName().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(excelFileStream);
            }
            else {
                return null;
            }

            if (workbook.getNumberOfSheets() == 0) {
                return null;
            }
            else {
                return workbook.getSheetAt(0);
            }

        }
        catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        return null;
    }

    private List<Cell> getCellsFromSheet(Sheet sheet) {
        if (sheet == null) {
            return new ArrayList<>();
        }

        List<Cell> cells = new ArrayList<>();
        for (Row row : sheet) {
            for (Cell cell : row)
                cells.add(cell);
        }
        return cells;
    }

    /*
    @Test
    public void parseFileWithoutStorage() {
        Sheet sheet = getSheet(fileWithoutStorage);
        HeaderParser headerParser = new HeaderParser(sheet, 12);

        Map<String, List<Cell>> headerCells = headerParser.findHeaderCells();


    }

    @Test
    public void parseFileWithOneStorage() {
        Sheet sheet = getSheet(fileWithOneStorage);
        HeaderParser headerParser = new HeaderParser(sheet, 12);

        //Map<String, List<Cell>> headerParser.findHeaderCells();
    }

    @Test
    public void parseFileWithSeveralSimpleStorages() {
        Sheet sheet = getSheet(fileWithSeveralSimpleStorages);
        HeaderParser headerParser = new HeaderParser(sheet, 12);
    }

    @Test
    public void parseFileWithSeveralComplexStorages() {
        Sheet sheet = getSheet(fileWithSeveralComplexStorages);
        HeaderParser headerParser = new HeaderParser(sheet, 12);




    }

     */

    @Test
    public void findMaxRowIndex() {
        Sheet sheet = getSheet(fileForFindingHeaderCells);
        List<Cell> cells = getCellsFromSheet(sheet);

        int result = HeaderParser.findMaxRowIndex(cells);

        assertEquals(1, result);
    }




    /*
    @Test

    public void getCellsOnRow() {
        Sheet sheet = getSheet(fileForFindingHeaderCells);
        HeaderParser headerParser = new HeaderParser(sheet, 12);
        List<Cell> cells = getCellsFromSheet(sheet);



        expectedCells =
    }

     */


}
