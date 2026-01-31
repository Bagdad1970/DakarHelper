package io.github.bagdad.excelparser.utils;

import io.github.bagdad.models.excelparser.Category;
import org.apache.poi.ss.usermodel.Cell;

import java.util.*;
import java.util.stream.Collectors;

public class HeaderParserUtils {

    public static Map<Category, Integer> findMaxRowIndexInGroups(Map<Category, List<Cell>> cells) {
        if (cells.isEmpty()) {
            return Map.of();
        }

        Map<Category, Integer> maxRowForCategories = new HashMap<>();
        for (Category category : cells.keySet()) {
            List<Cell> cellsByCategory = cells.get(category);

            int maxRowIndexInCells = findMaxRowIndexInCells(cellsByCategory);

            if (maxRowIndexInCells != -1) {
                maxRowForCategories.put(category, maxRowIndexInCells);
            }
        }

        return maxRowForCategories;
    }

    public static int findMaxRowIndex(Map<Category, Integer> maxRowIndexForCategories) {
        return maxRowIndexForCategories.values().stream()
                .max(Integer::compareTo)
                .orElse(-1);
    }

    public static int findMaxRowIndexInCells(List<Cell> cells) {
        return cells.stream()
                .mapToInt(Cell::getRowIndex)
                .max()
                .orElse(-1);
    }

    public static Map<Category, Map<Integer, List<Cell>>> groupByColumn(Map<Category, List<Cell>> cellsGroupedByCategory) {
        if (cellsGroupedByCategory.isEmpty()) {
            return new HashMap<>();
        }

        Map<Category, Map<Integer, List<Cell>>> groupedCells = new HashMap<>();
        for (Category category : cellsGroupedByCategory.keySet()) {
            List<Cell> cells = cellsGroupedByCategory.get(category);
            Map<Integer, List<Cell>> groupedCellsForKey = cells.stream()
                    .collect(Collectors.groupingBy(Cell::getColumnIndex));
            groupedCells.put(category, groupedCellsForKey);
        }
        return groupedCells;
    }

    public static Map<Category, Map<Integer, List<Cell>>> removeGroupsWithoutMaxRow(Map<Category, Integer> maxRowIndexForCategories, Map<Category, Map<Integer, List<Cell>>> groupedCellsByColumn) {
        if (maxRowIndexForCategories.isEmpty()) {
            return new HashMap<>();
        }

        if (groupedCellsByColumn == null || groupedCellsByColumn.isEmpty()) {
            return new HashMap<>();
        }

        Map<Category, Map<Integer, List<Cell>>> cellGroupsWithMaxRow = new HashMap<>();
        for (var keyEntry : groupedCellsByColumn.entrySet()) {
            Category category = keyEntry.getKey();
            int maxRowIndex = maxRowIndexForCategories.get(category);

            if (keyEntry.getValue().size() == 1) {
                cellGroupsWithMaxRow.put(category, keyEntry.getValue());
            }
            else {
                Map<Integer, List<Cell>> filteredColumns = new HashMap<>();
                for (var columnEntry : keyEntry.getValue().entrySet()) {
                    List<Cell> cells = columnEntry.getValue();
                    if (HeaderParserUtils.findMaxRowIndexInCells(cells) == maxRowIndex) {
                        filteredColumns.put(columnEntry.getKey(), cells);
                    }
                }

                if (!filteredColumns.isEmpty()) {
                    cellGroupsWithMaxRow.put(category, filteredColumns);
                }
            }
        }

        return cellGroupsWithMaxRow;
    }

}
