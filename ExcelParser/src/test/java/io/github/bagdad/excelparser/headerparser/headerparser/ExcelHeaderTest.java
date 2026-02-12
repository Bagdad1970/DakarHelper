package io.github.bagdad.excelparser.headerparser.headerparser;

import io.github.bagdad.excelparser.headerparser.model.ExcelProduct;
import io.github.bagdad.excelparser.headerparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.headerparser.columns.NameColumn;
import io.github.bagdad.excelparser.headerparser.headerparser.columns.PriceColumn;
import io.github.bagdad.excelparser.headerparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.excelparser.headerparser.model.Storage;
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

        String[][] cells = {
                {"",      "price",     "price",  "price",    "Storage 1", "Storage 2"},
                {"name",  "wholesale", "retail", "internet", "count",     "count"},
                {"name1", "123.45",    "130",    "150",      "10",        "20"}
        };

        for (int i = 0; i < cells.length; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < cells[0].length; j++) {
                CellUtil.createCell(row, j, cells[i][j]);
            }
        }

        Map<Category, Set<Column>> headerColumns = Map.of(
                Category.NAME, Set.of(new NameColumn(0, "name")),
                Category.PRICE, Set.of(
                        new PriceColumn(1, "wholesale"),
                        new PriceColumn(2, "retail"),
                        new PriceColumn(3, "internet")),
                Category.QUANTITY, Set.of(
                        new QuantityColumn(4, "count1", new Storage("storage1", "Storage 1")),
                        new QuantityColumn(5, "count2", new Storage("storage2", "Storage 2"))
                ));
        excelHeader = new ExcelHeader(2, headerColumns);
    }

    @Test
    void processRow() {
        Row row = sheet.getRow(2);

        ExcelProduct result = excelHeader.processRow(row);

        ExcelProduct expectedExcelProduct = new ExcelProduct();
        expectedExcelProduct.setNames(Map.of(
                "name", "name1")
        );
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

