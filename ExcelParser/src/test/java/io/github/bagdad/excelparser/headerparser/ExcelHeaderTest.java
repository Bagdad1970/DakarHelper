package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.model.Product;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.NameColumn;
import io.github.bagdad.excelparser.headerparser.columns.PriceColumn;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.models.excelparser.Category;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExcelHeaderTest {

    private static Workbook workbook;
    private static Sheet sheet;
    private static ExcelHeader excelHeader;

    @BeforeAll
    static void setupSheet() throws IOException {
        workbook = WorkbookFactory.create(true);
        sheet = workbook.createSheet();

        sheet.createRow(0);
        sheet.createRow(1);
        Row bodyRow = sheet.createRow(2);

        String[][] headerCells = {
                {"",             "цена", "цена",    "цена",     "Склад 1", "Склад 2"},
                {"номенклатура", "опт",  "розница", "интернет", "остаток", "остаток"}
        };

        String[] bodyCells = {"LDF 123/45 6.8", "123.45", "130", "150", "10", "20"};

        for (int i = 0; i < headerCells.length; i++) {
            for (int j = 0; j < headerCells[0].length; j++) {
                CellUtil.createCell(sheet.getRow(i), j, headerCells[i][j]);
            }
        }

        for (int i = 0; i < bodyCells.length; i++) {
            CellUtil.createCell(bodyRow, i, bodyCells[i]);
        }

        Map<Category, Set<Column>> headerColumns = Map.of(
                Category.NAME, Set.of(new NameColumn(0, "номенклатура")),
                Category.PRICE, Set.of(
                        new PriceColumn(1, "опт"),
                        new PriceColumn(2, "розница"),
                        new PriceColumn(3, "интернет")),
                Category.QUANTITY, Set.of(
                        new QuantityColumn(4, "count1", new Storage("storage1", "Склад 1")),
                        new QuantityColumn(5, "count2", new Storage("storage2", "Склад 2"))
        ));
        excelHeader = new ExcelHeader(2, headerColumns);
    }

    @Test
    void processRow() {
        Row row = sheet.getRow(2);

        Product result = excelHeader.processRow(row);

        Product expectedProduct = new Product();
        expectedProduct.setNames(Map.of(
                "номенклатура", "LDF 123/45 6.8")
        );
        expectedProduct.setPrices(Map.of(
            "опт", BigDecimal.valueOf(123.45),
            "розница", BigDecimal.valueOf(130.0),
            "интернет", BigDecimal.valueOf(150.0)
        ));
        expectedProduct.setQuantities(Map.of(
                "count1", 10,
                "count2", 20
        ));

        assertEquals(expectedProduct, result);
    }

}
