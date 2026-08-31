package io.github.bagdad.excelparser;

import io.github.bagdad.excelparser.headerparser.ParserFactory;
import io.github.bagdad.excelparser.model.ExcelProduct;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExcelParserTest extends SheetTest {

    private static ExcelHeaderCellsHandler excelHeaderCellsHandler;

    private static ParserFactory parserFactory;

    @BeforeAll
    static void setupExcelHeaderCellsHandler() {
        List<HeaderCellDto> headerCellDtos = new ArrayList<>();
        headerCellDtos.add(new HeaderCellDto("артикул", Category.ARTICLE, "артикул", true));
        headerCellDtos.add(new HeaderCellDto("номенклатура", Category.NAME, "номенклатура", true));
        headerCellDtos.add(new HeaderCellDto("наименование", Category.NAME, "наименование", true));
        headerCellDtos.add(new HeaderCellDto("цена", Category.PRICE, "цена", true));
        headerCellDtos.add(new HeaderCellDto("опт", Category.PRICE, "опт", true));
        headerCellDtos.add(new HeaderCellDto("розница", Category.PRICE, "розница", true));
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
                "опт", List.of("опт"),
                "розница", List.of("розница")
        );
        Map<String, List<String>> quantityMap = Map.of(
                "склад", List.of("склад", "остаток")
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
    void Parsing_valid_table_must_return_valid_products() {
        String[][] table = {
                {"номенклатура", "",             "опт",    "розница", "Склад 1", "Склад 2"},
                {"артикул",      "номенклатура", "цена",   "цена",    "остаток", "остаток"},
                {"0001",         "name1",        "100.00", "125.85",  "10",      "7"},
                {"0002",         "name2",        "250.36", "300.75",  "5",       "2"},
                {"0003",         "name3",        "24.36",  "29.39",   "1",       "0"}
        };
        Sheet sheet = createSheet(table);

        ExcelParser sut = new ExcelParser(sheet, excelHeaderCellsHandler, parserFactory);
        sut.tryToParse();
        List<ExcelProduct> excelProducts = sut.parse();

        ExcelProduct excelProduct1 = ExcelProduct.builder()
                .article("0001")
                .name("name1")
                .prices(Map.of("опт", BigDecimal.valueOf(100.00), "розница", BigDecimal.valueOf(125.85)))
                .quantities(Map.of("склад1", 10, "склад2", 7))
                .build();

        ExcelProduct excelProduct2 = ExcelProduct.builder()
                .article("0002")
                .name("name2")
                .prices(Map.of("опт", BigDecimal.valueOf(250.36), "розница", BigDecimal.valueOf(300.75)))
                .quantities(Map.of("склад1", 5, "склад2", 2))
                .build();

        ExcelProduct excelProduct3 = ExcelProduct.builder()
                .article("0003")
                .name("name3")
                .prices(Map.of("опт", BigDecimal.valueOf(24.36), "розница", BigDecimal.valueOf(29.39)))
                .quantities(Map.of("склад1", 1, "склад2", 0))
                .build();
        List<ExcelProduct> expectedExcelProducts = List.of(excelProduct1, excelProduct2, excelProduct3);
        expectedExcelProducts.forEach(ExcelProduct::compute);

        assertThat(excelProducts).isEqualTo(expectedExcelProducts);
    }

}
