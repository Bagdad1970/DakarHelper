package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.NameColumn;
import io.github.bagdad.excelparser.utils.ExcelCellProcessor;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.*;

@Slf4j
public class NameParser implements Parser {

    private final SubcategoryMapping subcategoryMapping;

    public NameParser(SubcategoryMapping subcategoryMapping) {
        this.subcategoryMapping = subcategoryMapping;
    }

    @Override
    public Set<Column> parseColumns(Map<Integer, List<Cell>> cellsByClass) {
        log.info("Parsing name columns");

        if (cellsByClass.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Column> columns = new HashSet<>();
        for (int columnIndex : cellsByClass.keySet()) {
            List<Cell> columnCells = cellsByClass.get(columnIndex);

            for (Cell cell : columnCells) {
                String cellValue = ExcelCellProcessor.getNormalizedCellValue(cell);
                String columnName = subcategoryMapping.getKeyByValue(cellValue);
                if (columnName != null) {
                    columns.add(new NameColumn(columnIndex, columnName));
                }
            }
        }

        return columns;
    }
}
