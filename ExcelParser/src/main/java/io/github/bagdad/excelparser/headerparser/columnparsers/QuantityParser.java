package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.Storage;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.excelparser.utils.ExcelCellProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.*;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
public class QuantityParser implements Parser {

    private final SubcategoryMapping subcategoryMapping;

    @Override
    public Set<Column> parseColumns(Map<Integer, List<Cell>> cellsByClass) {
        log.info("Parsing quantity columns");

        if (cellsByClass.isEmpty()) {
            return new HashSet<>();
        }

        Map<String, List<Cell>> groupedCells = groupCellsBySubcategory(cellsByClass);

        Set<Column> result = new HashSet<>();
        for (Map.Entry<String, List<Cell>> entry : groupedCells.entrySet()) {
            String subcategory = entry.getKey();
            List<Cell> cells = entry.getValue();
            result.addAll(arrangeSubcategoryValues(subcategory, cells));
        }

        return result;
    }

    Map<String, List<Cell>> groupCellsBySubcategory(Map<Integer, List<Cell>> cellsByClass) {
        if (cellsByClass == null || cellsByClass.isEmpty()) {
            return Map.of();
        }

        Map<String, List<Cell>> grouped = new HashMap<>();
        for (List<Cell> columnCells : cellsByClass.values()) {
            for (Cell cell : columnCells) {
                String normalizedValue = ExcelCellProcessor.getNormalizedCellValue(cell);
                String subcategory = subcategoryMapping.getKeyByValue(normalizedValue);
                if (subcategory != null) {
                    grouped.computeIfAbsent(subcategory, _ -> new ArrayList<>()).add(cell);
                }
            }
        }

        return grouped;
    }

    Set<Column> arrangeSubcategoryValues(String subcategory, List<Cell> cells) {
        if (cells == null || cells.isEmpty()) {
            return new HashSet<>();
        }

        Set<Column> columns = new HashSet<>();
        if (cells.size() == 1) {
            int columnIndex = cells.getFirst().getColumnIndex();
            String key = subcategory + "1";
            String storageName = subcategory + " 1";
            Storage storage = new Storage(key, storageName);
            columns.add(new QuantityColumn(columnIndex, key, storage));

            return columns;
        }

        cells.sort(Comparator.comparingInt(Cell::getColumnIndex));

        int minColumnIndex = cells.getFirst().getColumnIndex();

        for (Cell cell : cells) {
            int sequenceNumber = cell.getColumnIndex() - minColumnIndex + 1;
            String key = subcategory + sequenceNumber;
            String storageName = subcategory + " " + sequenceNumber;
            Storage storage = new Storage(key, storageName);
            columns.add(new QuantityColumn(cell.getColumnIndex(), key, storage));
        }

        return columns;
    }
}