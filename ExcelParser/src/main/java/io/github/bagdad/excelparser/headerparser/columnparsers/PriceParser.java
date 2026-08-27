package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.columns.CategoryColumn;
import io.github.bagdad.excelparser.headerparser.columns.PriceColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.excelparser.utils.ExcelCellUtils;
import io.github.bagdad.models.excelparser.Category;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class PriceParser extends CategoryParser {

    public PriceParser(SubcategoryMapping subcategoryMapping) {
        super(subcategoryMapping);
    }

    public static BigDecimal processCell(Cell cell) {
        String cellValue = ExcelCellUtils.getRawCellValue(cell);
        if (ExcelCellUtils.isCellValueEmpty(cellValue))
            return null;

        try {
            double value = Double.parseDouble(cellValue);
            return value > 0 ? BigDecimal.valueOf(value) : null;
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Set<CategoryColumn> parseColumns(Map<Integer, List<Cell>> cellsByClass) {
        log.info("Parsing price columns");

        if (cellsByClass.isEmpty()) {
            return new HashSet<>();
        }

        Set<CategoryColumn> columns = new HashSet<>();
        for (int columnIndex : cellsByClass.keySet()) {
            List<Cell> columnCells = cellsByClass.get(columnIndex);

            String columnName = Category.PRICE.getName();
            for (Cell cell : columnCells) {
                String cellValue = ExcelCellUtils.getNormalizedCellValue(cell);
                String foundColumnName = subcategoryMapping.getKeyByValue(cellValue);

                if (foundColumnName != null) {
                    columnName = foundColumnName;
                }
            }

            columns.add(new PriceColumn(columnIndex, columnName));
        }

        return columns;
    }

}
