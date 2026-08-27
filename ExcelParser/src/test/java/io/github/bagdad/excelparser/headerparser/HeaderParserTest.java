package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.SheetTest;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


public class HeaderParserTest extends SheetTest {

    private static ExcelHeaderCellsHandler excelHeaderCellsHandler;

    private static ParserFactory parserFactory;

    @BeforeAll
    static void setupExcelHeaderCellsHandler() {
        List<HeaderCellDto> headerCellDtos = new ArrayList<>();
        headerCellDtos.add(new HeaderCellDto("код", Category.ARTICLE, "артикул", true));
        headerCellDtos.add(new HeaderCellDto("номенклатура", Category.NAME, "номенклатура", true));
        headerCellDtos.add(new HeaderCellDto("наименование", Category.NAME, "наименование", true));
        headerCellDtos.add(new HeaderCellDto("цена", Category.PRICE, "цена", true));
        headerCellDtos.add(new HeaderCellDto("остаток", Category.QUANTITY, "остаток", true));
        headerCellDtos.add(new HeaderCellDto("склад", Category.QUANTITY, "склад", true));
        excelHeaderCellsHandler = new ExcelHeaderCellsHandler(headerCellDtos);
    }

    @BeforeAll
    static void setupParserFactory() {
        Map<String, List<String>> articleMap = Map.of(
                "артикул", List.of("код", "артикул")
        );
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
        SubcategoryMapping articleMapping = new SubcategoryMapping(articleMap);
        SubcategoryMapping nameMapping = new SubcategoryMapping(nameMap);
        SubcategoryMapping priceMapping = new SubcategoryMapping(priceMap);
        SubcategoryMapping quantityMapping = new SubcategoryMapping(quantityMap);

        parserFactory = new ParserFactory();
        parserFactory.addMapping(Category.ARTICLE, articleMapping);
        parserFactory.addMapping(Category.NAME, nameMapping);
        parserFactory.addMapping(Category.PRICE, priceMapping);
        parserFactory.addMapping(Category.QUANTITY, quantityMapping);
    }

    @Test
    void Finding_header_cells_with_all_known_cells_must_fill_the_field() {
        String[][] header = {
                {"номенклатура", "",    "цена", "Склад 1", "Склад 2"},
                {"код", "номенклатура", "цена", "остаток", "остаток"}
        };
        Sheet sheet = createSheet(header);

        HeaderParser sut = new HeaderParser(sheet, excelHeaderCellsHandler, parserFactory);

        sut.tryToFindHeaderCells();
        Map<Category, List<Cell>> result = sut.getCellsGroupedByCategory();

        Map<Category, List<Cell>> expected = Map.of(
                Category.ARTICLE, List.of(
                        sheet.getRow(1).getCell(0)
                ),
                Category.NAME, List.of(
                        sheet.getRow(0).getCell(0),
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

    @Test
    void Finding_header_cells_without_article_must_fill_the_field() {
        String[][] header = {
                {"номенклатура", "цена", "Склад 1", "Склад 2"},
                {"номенклатура", "цена", "остаток", "остаток"}
        };
        Sheet sheet = createSheet(header);

        HeaderParser sut = new HeaderParser(sheet, excelHeaderCellsHandler, parserFactory);

        sut.tryToFindHeaderCells();
        Map<Category, List<Cell>> result = sut.getCellsGroupedByCategory();

        Map<Category, List<Cell>> expected = Map.of(
                Category.NAME, List.of(
                        sheet.getRow(0).getCell(0),
                        sheet.getRow(1).getCell(0)
                ),
                Category.PRICE, List.of(
                        sheet.getRow(0).getCell(1),
                        sheet.getRow(1).getCell(1)
                ),
                Category.QUANTITY, List.of(
                        sheet.getRow(0).getCell(2),
                        sheet.getRow(0).getCell(3),
                        sheet.getRow(1).getCell(2),
                        sheet.getRow(1).getCell(3)
                )
        );

        assertThat(result).isEqualTo(expected);
        assertThat(sut.getUnprocessableHeaderCells()).isEmpty();
    }

}
