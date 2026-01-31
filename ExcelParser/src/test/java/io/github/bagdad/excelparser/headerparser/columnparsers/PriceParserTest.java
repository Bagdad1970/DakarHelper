package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.PriceColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceParserTest {

    private static Workbook workbook;
    private static Sheet sheet;

    @BeforeAll
    static void setupSheet() throws IOException {
        workbook = WorkbookFactory.create(true);
        sheet = workbook.createSheet();

        Row row0 = sheet.createRow(0);
        Row row1 = sheet.createRow(1);

        String[] keywords = {"оптовая", "розничная", "интернет"};

        for (int i = 0; i < keywords.length; i++) {
            CellUtil.createCell(row0, i, keywords[i]);
        }

        for (int i = 0; i < keywords.length; i++) {
            CellUtil.createCell(row1, i, "цена");
        }
    }

    @Test
    void parseColumns() {
        Map<String, List<String>> priceMap = Map.of(
                "опт", List.of("оптовая"),
                "розница", List.of("розничная"),
                "интернет", List.of("интернет")
        );
        SubcategoryMapping subcategoryMapping = new SubcategoryMapping(priceMap);
        PriceParser priceParser = new PriceParser(subcategoryMapping);

        List<Cell> list1 = Arrays.asList(sheet.getRow(0).getCell(0), sheet.getRow(1).getCell(0));
        List<Cell> list2 = Arrays.asList(sheet.getRow(0).getCell(1), sheet.getRow(1).getCell(1));
        List<Cell> list3 = Arrays.asList(sheet.getRow(0).getCell(2), sheet.getRow(1).getCell(2));
        Map<Integer, List<Cell>> columns = new HashMap<>();
        columns.put(0, list1);
        columns.put(1, list2);
        columns.put(2, list3);


        Set<Column> headerColumns = priceParser.parseColumns(columns);

        Set<Column> expected = Set.of(
            new PriceColumn(0, "опт"),
            new PriceColumn(1, "розница"),
            new PriceColumn(2, "интернет")
        );

        assertEquals(expected, headerColumns);
    }


}
