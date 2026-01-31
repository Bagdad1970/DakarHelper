package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.NameColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NameParserTest {

    private static Workbook workbook;
    private static Sheet sheet;

    @BeforeAll
    static void setupSheet() throws IOException {
        workbook = WorkbookFactory.create(true);
        sheet = workbook.createSheet();

        Row row = sheet.createRow(0);

        String[] keywords = {"артикул", "номенклатура"};

        for (int i = 0; i < keywords.length; i++) {
            CellUtil.createCell(row, i, keywords[i]);
        }
    }

    @Test
    void parseColumns() {
        Map<String, List<String>> nameMap = Map.of(
                "артикул", List.of("артикул"),
                "номенклатура", List.of("наименование", "номенклатура")
        );
        SubcategoryMapping subcategoryMapping = new SubcategoryMapping(nameMap);
        NameParser sut = new NameParser(subcategoryMapping);

        Map<Integer, List<Cell>> columns = Map.of(
                0, List.of(sheet.getRow(0).getCell(0)),
                1, List.of(sheet.getRow(0).getCell(1))
        );

        Set<Column> headerColumns = sut.parseColumns(columns);

        Set<Column> expected = Set.of(
                new NameColumn(0, "артикул"),
                new NameColumn(1, "номенклатура")
        );

        assertEquals(expected, headerColumns);
    }

}
