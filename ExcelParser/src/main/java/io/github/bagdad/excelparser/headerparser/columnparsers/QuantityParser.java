package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.model.Storage;
import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.excelparser.utils.ExcelCellUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class QuantityParser extends CategoryParser {

    private static final String MORE_QUANTITY_WORD = "более";

    private static final String MORE_QUANTITY_SYMBOL = ">";

    public QuantityParser(SubcategoryMapping subcategoryMapping) {
        super(subcategoryMapping);
    }

    public static Integer processCell(Cell cell) {
        String cellValue = ExcelCellUtils.getNormalizedCellValue(cell);
        if (ExcelCellUtils.isCellValueEmpty(cellValue))
            return null;

        try {
            int result;

            // what if will be "1>23". it is 123 or 23?
            if (cellValue.contains(MORE_QUANTITY_SYMBOL) || cellValue.contains(MORE_QUANTITY_WORD)) {
                String numericValue = cellValue.replaceAll("\\D+", "");
                result = (int) Double.parseDouble(numericValue) + 1;
            }
            else {
                result = (int) Double.parseDouble(cellValue);
            }

            return Math.max(result, 0);
        }
        catch (NumberFormatException e) {
            return 0;
        }

    }

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
                String normalizedValue = ExcelCellUtils.getNormalizedCellValue(cell);
                String subcategory = subcategoryMapping.getKeyByValue(normalizedValue);
                if (subcategory != null) {
                    grouped.computeIfAbsent(subcategory, _ -> new ArrayList<>()).add(cell);
                }
            }
        }

        return grouped;
    }

    Set<Column> arrangeSubcategoryValues(String subcategory, List<Cell> cells) {
        if (cells.isEmpty()) {
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

    private String createKey(String key) {
        return key + 1;
    }

    private String createKey(String key, int number) {
        return key + number;
    }

    private String createStorage(String name) {
        return name + 1;
    }

    private String createStorage(String name, int number) {
        return name + number;
    }

}