package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.SheetTest;
import io.github.bagdad.excelparser.headerparser.columns.ArticleColumn;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ArticleParserTest extends SheetTest {

    @Test
    void parseColumns() {
        String[] keywords = {"артикул", "номенклатура"};
        Sheet sheet = createSheet(keywords);

        Map<String, List<String>> mapping = Map.of(
                "артикул", List.of("артикул")
        );
        SubcategoryMapping articleMapping = new SubcategoryMapping(mapping);

        Map<Integer, List<Cell>> columns = Map.of(
                0, List.of(sheet.getRow(0).getCell(0)),
                1, List.of(sheet.getRow(0).getCell(1))
        );

        ArticleParser sut = new ArticleParser(articleMapping);
        Set<Column> result = sut.parseColumns(columns);

        Set<Column> expected = Set.of(
                new ArticleColumn(0, "артикул")
        );

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void Processing_null_cell_must_return_null() {
        String value = null;
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        String result = ArticleParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_empty_cell_must_return_null() {
        String value = "";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        String result = ArticleParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_empty_cell_with_whitespaces_must_return_null() {
        String value = "    ";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        String result = ArticleParser.processCell(cell);

        assertThat(result).isNull();
    }

    @Test
    void Processing_valid_cell_must_return_its_string() {
        String value = "some_article";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        String result = ArticleParser.processCell(cell);

        assertThat(result).isEqualTo("some_article");
    }

    @Test
    void Processing_valid_cell_with_whitespaces_must_return_its_string_without_whitespaces() {
        String value = "  some_article  ";
        Sheet sheet = createSheet(value);
        Cell cell = sheet.getRow(0).getCell(0);

        String result = ArticleParser.processCell(cell);

        assertThat(result).isEqualTo("some_article");
    }

}
