package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.model.Storage;
import io.github.bagdad.excelparser.headerparser.columns.CategoryColumn;
import io.github.bagdad.excelparser.headerparser.columns.QuantityColumn;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import io.github.bagdad.excelparser.utils.ExcelCellUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.Collections;
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
    public Set<CategoryColumn> parseColumns(Map<Integer, List<Cell>> cellsByClass) {
        log.info("Parsing quantity columns");

        if (cellsByClass.isEmpty()) {
            return new HashSet<>();
        }

        Map<String, List<Cell>> groupedCells = groupCellsBySubcategory(cellsByClass);

        Set<CategoryColumn> columns = new HashSet<>();
        for (String subcategory : groupedCells.keySet()) {
            List<Cell> cells = groupedCells.get(subcategory);
            Set<CategoryColumn> arrangedColumns = arrangeSubcategoriesByNumber(subcategory, cells);
            columns.addAll(arrangedColumns);
        }

        return columns;
    }

    Map<String, List<Cell>> groupCellsBySubcategory(Map<Integer, List<Cell>> cellsByClass) {
        log.info("Grouping quantity cells by subcategory");

        if (cellsByClass == null || cellsByClass.isEmpty()) {
            return Collections.emptyMap();
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

    Set<CategoryColumn> arrangeSubcategoriesByNumber(String subcategory, List<Cell> cells) {
        if (cells.isEmpty()) {
            return new HashSet<>();
        }

        Set<CategoryColumn> columns = new HashSet<>();
        if (cells.size() == 1) {
            int columnIndex = cells.getFirst().getColumnIndex();
            columns.add(createQuantityColumn(subcategory, columnIndex, 1));

            return columns;
        }
        else {
            int minColumnIndex = cells.stream().mapToInt(Cell::getColumnIndex)
                    .min()
                    .orElseThrow();

            cells.forEach(cell -> {
                int sequenceNumber = cell.getColumnIndex() - minColumnIndex + 1;
                columns.add(createQuantityColumn(subcategory, cell.getColumnIndex(), sequenceNumber));
            });

            return columns;
        }
    }

    private QuantityColumn createQuantityColumn(String subcategory, int columnIndex, int sequenceNumber) {
        String key = subcategory + sequenceNumber;
        String storageName = subcategory + " " + sequenceNumber;
        Storage storage = new Storage(key, storageName);

        return new QuantityColumn(columnIndex, key, storage);
    }

}