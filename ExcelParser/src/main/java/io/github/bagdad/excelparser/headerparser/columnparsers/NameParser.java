package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.NameColumn;
import io.github.bagdad.excelparser.utils.ExcelCellProcessor;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import lombok.AllArgsConstructor;
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
        if (cellsByClass.isEmpty()) {
            return new HashSet<>();
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

//        if (cellsByClass.size() == 1) {
//            int columnIndex = cellsByClass.keySet().iterator().next();
//            columns.add(new NameColumn(columnIndex, "name"));
//        }
//        else {
//            cellsByClass.keySet().stream()
//                    .sorted()
//                    .forEach(columnIndex -> {
//                        List<Cell> cellsByColumn = cellsByClass.get(columnIndex);
//                        for (Cell cell : cellsByColumn) {
//                            String cellValue = ExcelCellProcessor.getNormalizedCellValue(cell);
//                            if (nameValues.contains(cellValue)) {
//                                columns.add(new NameColumn(columnIndex, "name"));
//                            }
//                        }
//                    });
//        }

        return columns;
    }
}
