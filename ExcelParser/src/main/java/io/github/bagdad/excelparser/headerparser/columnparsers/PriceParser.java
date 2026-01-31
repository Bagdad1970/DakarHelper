package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.PriceColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.excelparser.utils.ExcelCellProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.*;

@Slf4j
@AllArgsConstructor
public class PriceParser implements Parser {

    private final SubcategoryMapping subcategoryMapping;

    private static final String DEFAULT_COLUMN_NAME = "цена";

    @Override
    public Set<Column> parseColumns(Map<Integer, List<Cell>> cellsByClass) {
        log.info("Parsing price columns");

        if (cellsByClass.isEmpty()) {
            return new HashSet<>();
        }

        Set<Column> columns = new HashSet<>();
        for (int columnIndex : cellsByClass.keySet()) {
            List<Cell> columnCells = cellsByClass.get(columnIndex);

            String columnName = DEFAULT_COLUMN_NAME;
            for (Cell cell : columnCells) {
                String cellValue = ExcelCellProcessor.getNormalizedCellValue(cell);
                String foundedColumnName = subcategoryMapping.getKeyByValue(cellValue);
                if (foundedColumnName != null) {
                    columnName = foundedColumnName;
                }
            }

            columns.add(new PriceColumn(columnIndex, columnName));
        }

        return columns;
    }
}
