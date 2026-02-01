package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.CellStatus;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;


public class HeaderParserTest {

    private static Workbook workbook;
    private static ExcelHeaderCellsHandler excelHeaderCellsHandler;
    private static ParserFactory parserFactory;

    @BeforeAll
    static void setupExcelHeaderCellsHandler() {
        List<HeaderCellDto> headerCellDtos = new ArrayList<>();
        headerCellDtos.add(new HeaderCellDto("номенклатура", Category.NAME, null, CellStatus.PROCESSED));
        headerCellDtos.add(new HeaderCellDto("наименование", Category.NAME, null, CellStatus.PROCESSED));
        headerCellDtos.add(new HeaderCellDto("цена", Category.PRICE, null, CellStatus.PROCESSED));
        headerCellDtos.add(new HeaderCellDto("остаток", Category.QUANTITY, null, CellStatus.PROCESSED));
        headerCellDtos.add(new HeaderCellDto("склад", Category.QUANTITY, null, CellStatus.PROCESSED));
        excelHeaderCellsHandler = new ExcelHeaderCellsHandler(headerCellDtos);
    }

    @BeforeAll
    static void setupParserFactory() {
        Map<String, List<String>> nameMap = Map.of(
                "номенклатура", List.of("наименование", "номенклатура")
        );
        Map<String, List<String>> priceMap = Map.of(
                "опт", List.of("оптовый"),
                "к опт", List.of("к опт")
        );
        Map<String, List<String>> quantityMap = Map.of(
                "склад", List.of("склад")
        );
        SubcategoryMapping nameMapping = new SubcategoryMapping(nameMap);
        SubcategoryMapping priceMapping = new SubcategoryMapping(priceMap);
        SubcategoryMapping quantityMapping = new SubcategoryMapping(quantityMap);

        parserFactory = new ParserFactory(nameMapping, priceMapping, quantityMapping);
    }

    @BeforeAll
    static void setupSheetWithAllKnownCells() throws IOException {
        workbook = WorkbookFactory.create(true);
        Sheet sheet = workbook.createSheet("with_all_known_cells");

        String[][] headerCells = {
                {"номенклатура", "",             "цена", "Склад 1", "Склад 2"},
                {"наименование", "номенклатура", "цена", "остаток", "остаток"}
        };

        for (int i=0; i<headerCells.length; i++) {
            Row row = sheet.createRow(i);
            for (int j=0; j<headerCells[0].length; j++) {
                CellUtil.createCell(row, j, headerCells[row.getRowNum()][j]);
            }
        }
    }

    @Test
    void Finding_header_cells_with_all_known_cells_must_fill_the_field() {
        Sheet sheet = workbook.getSheet("with_all_known_cells");
        HeaderExtractor extractor = new HeaderExtractor(sheet);
        HeaderParser sut = new HeaderParser(extractor, excelHeaderCellsHandler, parserFactory);

        sut.tryToFindHeaderCells();
        Map<Category, List<Cell>> result = sut.getCellsGroupedByCategory();

        Map<Category, List<Cell>> expected = Map.of(
                Category.NAME, List.of(
                        sheet.getRow(0).getCell(0),
                        sheet.getRow(1).getCell(0),
                        sheet.getRow(1).getCell(1)
                ),
                Category.PRICE, List.of(
                        sheet.getRow(0).getCell(2),
                        sheet.getRow(1).getCell(2)
                ),
                Category.QUANTITY, List.of(
                        sheet.getRow(0).getCell(3),
                        sheet.getRow(0).getCell(4),
                        sheet.getRow(1).getCell(3),
                        sheet.getRow(1).getCell(4)
                )
        );

        assertThat(result).isEqualTo(expected);
        assertThat(sut.getUnprocessableHeaderCells()).isEmpty();
    }

}
