package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.SheetTest;
import io.github.bagdad.excelparser.model.ExcelProduct;
import io.github.bagdad.excelparser.headerparser.columns.CategoryColumn;
import io.github.bagdad.excelparser.headerparser.columns.NameColumn;
import io.github.bagdad.excelparser.headerparser.columns.PriceColumn;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.excelparser.model.Storage;
import io.github.bagdad.models.excelparser.Category;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExcelHeaderTest extends SheetTest {

    private static Map<Category, Set<CategoryColumn>> headerColumns;

    @BeforeAll
    static void setupSheet() {
        headerColumns = Map.of(
            Category.NAME, Set.of(new NameColumn(0, "name")),
            Category.PRICE, Set.of(
                    new PriceColumn(1, "wholesale"),
                    new PriceColumn(2, "retail"),
                    new PriceColumn(3, "internet")),
            Category.QUANTITY, Set.of(
                    new QuantityColumn(4, "count1", new Storage("storage1", "Storage 1")),
                    new QuantityColumn(5, "count2", new Storage("storage2", "Storage 2"))
            )
        );
    }

    @Test
    void processRow() {
        String[][] cells = {
                {"",      "price",     "price",  "price",    "Storage 1", "Storage 2"},
                {"name",  "wholesale", "retail", "internet", "count",     "count"},
                {"name1", "123.45",    "130",    "150",      "10",        "20"}
        };
        Sheet sheet = createSheet(cells);
        Row row = sheet.getRow(2);

        ExcelHeader excelHeader = new ExcelHeader(2, headerColumns);
        ExcelProduct result = excelHeader.processRow(row);

        ExcelProduct expectedExcelProduct = new ExcelProduct();
        expectedExcelProduct.setName("name1");
        expectedExcelProduct.setPrices(Map.of(
                "wholesale", BigDecimal.valueOf(123.45),
                "retail", BigDecimal.valueOf(130.0),
                "internet", BigDecimal.valueOf(150.0)
        ));
        expectedExcelProduct.setQuantities(Map.of(
                "count1", 10,
                "count2", 20
        ));

        assertEquals(expectedExcelProduct, result);
    }

}

