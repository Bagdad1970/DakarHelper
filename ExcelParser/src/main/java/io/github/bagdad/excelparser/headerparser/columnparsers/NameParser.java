package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.exception.UnableProcessCellException;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.NameColumn;
import io.github.bagdad.excelparser.utils.ExcelCellUtils;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class NameParser implements Parser {

    private final SubcategoryMapping subcategoryMapping;

    public NameParser(SubcategoryMapping subcategoryMapping) {
        this.subcategoryMapping = subcategoryMapping;
    }

    public static String processCell(Cell cell) {
        String cellValue = ExcelCellUtils.getRawCellValue(cell);

        if (ExcelCellUtils.isCellValueEmpty(cellValue))
            return null;

        return cellValue.trim();
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
                String cellValue = ExcelCellUtils.getNormalizedCellValue(cell);
                String columnName = subcategoryMapping.getKeyByValue(cellValue);
                if (columnName != null) {
                    columns.add(new NameColumn(columnIndex, columnName));
                }
            }
        }

        return columns;
    }

}
