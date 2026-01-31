package io.github.bagdad.excelparser.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ExcelCellProcessorTest {

    private static Workbook workbook;
    private static Sheet sheet;

    @BeforeAll
    static void setupSheet() throws IOException {
        workbook = WorkbookFactory.create(true);
        sheet = workbook.createSheet();

        Row rowOfStringCellValues = sheet.createRow(0);
        Row rowOfNumericCellValues = sheet.createRow(1);
        Row rowOfNameCellValues = sheet.createRow(2);
        Row rowOfPriceCellValues = sheet.createRow(3);
        Row rowOfQuantityCellValues = sheet.createRow(4);

        String[] stringCellValues = {null, "", "  ", "LDF 123/45 6.8"};
        Double[] numericCellValues = {12.56, 12.0};
        String[] nameCellValues = {null, "", "  ", "LDF 123/45 6.8", "   LDF 123/45 6.8   "};
        String[] priceCellValues = {null, "", "    ", "12", "12.56", "   12.56   ", "0", "-1"};
        String[] quantityCellValues = {null, "", "12", "12.56", "   12.56   ", "  >12  ",  "0", "-1"};
        
        for (int i = 0; i<stringCellValues.length; i++) {
            CellUtil.createCell(rowOfStringCellValues, i, stringCellValues[i]);
        }

        for (int i = 0; i < numericCellValues.length; i++) {
            CellUtil.createCell(rowOfNumericCellValues, i, "");
            rowOfNumericCellValues.getCell(i).setCellValue(numericCellValues[i]);
        }

        for (int i = 0; i<nameCellValues.length; i++) {
            CellUtil.createCell(rowOfNameCellValues, i, nameCellValues[i]);
        }

        for (int i = 0; i<priceCellValues.length; i++) {
            CellUtil.createCell(rowOfPriceCellValues, i, priceCellValues[i]);
        }

        for (int i = 0; i<quantityCellValues.length; i++) {
            CellUtil.createCell(rowOfQuantityCellValues, i, quantityCellValues[i]);
        }
    }

    @Test
    void isCellValueEmpty() {
        String cellValue1 = "   ";
        String cellValue2 = "  NULL  ";
        String cellValue3 = "12.56";

        boolean isCellValueEmpty1 = ExcelCellProcessor.isCellValueEmpty(cellValue1);
        boolean isCellValueEmpty2 = ExcelCellProcessor.isCellValueEmpty(cellValue2);
        boolean isCellValueEmpty3 = ExcelCellProcessor.isCellValueEmpty(cellValue3);

        assertTrue(isCellValueEmpty1);
        assertTrue(isCellValueEmpty2);
        assertFalse(isCellValueEmpty3);
    }

    @Test
    void getRawCellValueForStringValues() {
        Row rowOfNumericCellValues = CellUtil.getRow(0, sheet);

        String cellValue1 = ExcelCellProcessor.getRawCellValue(rowOfNumericCellValues.getCell(0));
        String cellValue2 = ExcelCellProcessor.getRawCellValue(rowOfNumericCellValues.getCell(1));
        String cellValue3 = ExcelCellProcessor.getRawCellValue(rowOfNumericCellValues.getCell(2));
        String cellValue4 = ExcelCellProcessor.getRawCellValue(rowOfNumericCellValues.getCell(3));

        assertEquals("", cellValue1);
        assertEquals("", cellValue2);
        assertEquals("  ", cellValue3);
        assertEquals("LDF 123/45 6.8", cellValue4);
    }

    @Test
    void getRawCellValueForNumericValues() {
        Row rowOfNumericCellValues = CellUtil.getRow(1, sheet);

        String cellValue1 = ExcelCellProcessor.getRawCellValue(rowOfNumericCellValues.getCell(0));
        String cellValue2 = ExcelCellProcessor.getRawCellValue(rowOfNumericCellValues.getCell(1));

        assertEquals("12.56", cellValue1);
        assertEquals("12.0", cellValue2);
    }

    @Test
    void processEmptyNameCellValue() {
        Row rowOfNameCellValues = CellUtil.getRow(2, sheet);

        String processedValue1 = ExcelCellProcessor.processNameCell(CellUtil.getCell(rowOfNameCellValues, 0));
        String processedValue2 = ExcelCellProcessor.processNameCell(CellUtil.getCell(rowOfNameCellValues, 1));
        String processedValue3 = ExcelCellProcessor.processNameCell(CellUtil.getCell(rowOfNameCellValues, 2));

        assertNull(processedValue1);
        assertNull(processedValue2);
        assertNull(processedValue3);
    }

    @Test
    void processNameCellValueWithValue() {
        Row rowOfNameCellValues = CellUtil.getRow(2, sheet);

        String processedValue1 = ExcelCellProcessor.processNameCell(CellUtil.getCell(rowOfNameCellValues, 3));
        String processedValue2 = ExcelCellProcessor.processNameCell(CellUtil.getCell(rowOfNameCellValues, 4));

        assertEquals("LDF 123/45 6.8", processedValue1);
        assertEquals("LDF 123/45 6.8", processedValue2);
    }

    @Test
    void processEmptyPriceCellValue() {
        Row rowOfPriceCellValues = CellUtil.getRow(3, sheet);

        BigDecimal processedValue1 = ExcelCellProcessor.processPriceCell(CellUtil.getCell(rowOfPriceCellValues, 0));
        BigDecimal processedValue2 = ExcelCellProcessor.processPriceCell(CellUtil.getCell(rowOfPriceCellValues, 1));
        BigDecimal processedValue3 = ExcelCellProcessor.processPriceCell(CellUtil.getCell(rowOfPriceCellValues, 2));

        assertNull(processedValue1);
        assertNull(processedValue2);
        assertNull(processedValue3);
    }

    @Test
    void processPriceCellValueWithValue() {
        Row rowOfPriceCellValues = CellUtil.getRow(3, sheet);

        BigDecimal processedValue1 = ExcelCellProcessor.processPriceCell(CellUtil.getCell(rowOfPriceCellValues, 3));
        BigDecimal processedValue2 = ExcelCellProcessor.processPriceCell(CellUtil.getCell(rowOfPriceCellValues, 4));
        BigDecimal processedValue3 = ExcelCellProcessor.processPriceCell(CellUtil.getCell(rowOfPriceCellValues, 5));
        BigDecimal processedValue4 = ExcelCellProcessor.processPriceCell(CellUtil.getCell(rowOfPriceCellValues, 6));
        BigDecimal processedValue5 = ExcelCellProcessor.processPriceCell(CellUtil.getCell(rowOfPriceCellValues, 6));

        assertEquals(BigDecimal.valueOf(12.0), processedValue1);
        assertEquals(BigDecimal.valueOf(12.56), processedValue2);
        assertEquals(BigDecimal.valueOf(12.56), processedValue3);
        assertNull(processedValue4);
        assertNull(processedValue5);
    }

    @Test
    void processEmptyQuantityCellValue() {
        Row rowOfQuantityCellValues = CellUtil.getRow(4, sheet);

        Integer processedValue1 = ExcelCellProcessor.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 0));
        Integer processedValue2 = ExcelCellProcessor.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 1));

        assertNull(processedValue1);
        assertNull(processedValue2);
    }

    @Test
    void processQuantityCellWithValue() {
        Row rowOfQuantityCellValues = CellUtil.getRow(4, sheet);

        Integer processedValue1 = ExcelCellProcessor.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 2));
        Integer processedValue2 = ExcelCellProcessor.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 3));
        Integer processedValue3 = ExcelCellProcessor.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 4));
        Integer processedValue4 = ExcelCellProcessor.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 5));
        Integer processedValue5 = ExcelCellProcessor.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 6));
        Integer processedValue6 = ExcelCellProcessor.processQuantityCell(CellUtil.getCell(rowOfQuantityCellValues, 7));


        assertEquals(12, processedValue1);
        assertEquals(12, processedValue2);
        assertEquals(12, processedValue3);
        assertEquals(13, processedValue4);
        assertEquals(0, processedValue5);
        assertEquals(0, processedValue6);
    }

}
