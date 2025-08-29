package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.CountColumn;
import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.NameColumn;
import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.PriceColumn;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import java.util.*;
import java.util.stream.Collectors;


public class HeaderParser {

    private static Aliases aliases = new Aliases() {{
        addAliases("name", Arrays.asList("номенклатура", "номер"));
        addAliases("price", Arrays.asList("цена", "стоимость", "опт", "оптовая", "розница", "розничная", "интернет"));
        addAliases("count", Arrays.asList("остаток", "количество", "склад"));
    }};
    private Sheet sheet;
    private int limitRowCount;

    public HeaderParser(Sheet sheet, int limitRowCount) {
        this.sheet = sheet;
        this.limitRowCount = limitRowCount;
    }

    private Map<String, List<Cell>> findHeaderCells() {
        Map<String, List<Cell>> indexMap = new HashMap<>();

        for (int i = 0; i < limitRowCount; i++) {
            Row row = sheet.getRow(i);

            for (Cell cell : row) {
                String cellValue = cell.toString();

                for (String key : aliases.keySet()) {
                    if (aliases.hasAliasByKey(key, cellValue)) {
                        if ( !indexMap.containsKey(key) ) {
                            List<Cell> cells = new ArrayList<>();
                            cells.add(cell);
                            indexMap.put(key, cells);
                        }
                        else
                            indexMap.get(key).add(cell);

                        break;
                    }
                }
            }
        }

        Map<String, List<Cell>> clearedIndexMap = new HashMap<>();
        for (String key : indexMap.keySet()) {
            clearedIndexMap.put(key, leaveCellsOnMaxRowIndexes(indexMap.get(key)));
        }

        return clearedIndexMap;
    }

    private static Map<String, Map<Integer, List<Cell>>> groupByColumns(Map<String, List<Cell>> indexMap) {
        Map<String, Map<Integer, List<Cell>>> groupedMap = new HashMap<>();

        for (String key : indexMap.keySet()) {
            List<Cell> cellsByKey = indexMap.get(key);
            Map<Integer, List<Cell>> groupedCells = cellsByKey.stream().collect(Collectors.groupingBy(Cell::getColumnIndex));

            groupedMap.put(key, groupedCells);
        }
        return groupedMap;
    }

    private static List<Cell> leaveCellsOnMaxRowIndexes(List<Cell> cells) {
        if (cells.isEmpty())
            return new ArrayList<>();

        else if (cells.size() == 1)
            return cells;

        int maxRowIndex = cells.stream()
                .mapToInt(Cell::getRowIndex)
                .max()
                .orElse(-1);

        List<Cell> cellsWithNonMaxRow = getCellsWithNonMaxRow(maxRowIndex, cells);

        List<Integer> columnIndexesWithMaxRow = getColumnIndexesWithMaxRow(maxRowIndex, cells);

        List<Cell> cellsOnMaxRow = getCellsOnMaxRow(maxRowIndex, cells);

        for (Cell cell : cellsWithNonMaxRow) {
            if (columnIndexesWithMaxRow.contains(cell.getColumnIndex()))
                cellsOnMaxRow.add(cell);
        }

        return cellsOnMaxRow;
    }

    private static List<Cell> getCellsWithNonMaxRow(int maxRowIndex, List<Cell> cells) {
        return cells.stream()
                .filter(cell -> cell.getRowIndex() != maxRowIndex)
                .toList();
    }

    private static List<Integer> getColumnIndexesWithMaxRow(int maxRowIndex, List<Cell> cells) {
        return cells.stream()
                .filter(cell -> cell.getRowIndex() == maxRowIndex)
                .map(Cell::getColumnIndex)
                .toList();
    }

    private static List<Cell> getCellsOnMaxRow(int maxRowIndex, List<Cell> cells) {
        return cells.stream()
                .filter(cell -> cell.getRowIndex() == maxRowIndex)
                .toList();
    }

    public ExcelHeader parseHeader() {
        Map<String, List<Cell>> indexMap = findHeaderCells();

        Map<String, Map<Integer, List<Cell>>> groupedCellsByColumnIndex = HeaderParser.groupByColumns(indexMap);

        ExcelHeader excelHeader = new ExcelHeader();
        for (String key : groupedCellsByColumnIndex.keySet()) {
            Map<Integer, List<Cell>> cellsByKey = groupedCellsByColumnIndex.get(key);

            for (int columnIndex : cellsByKey.keySet()) {
                List<Cell> columnCells = cellsByKey.get(columnIndex);

                int maxRowIndex = columnCells.stream().mapToInt(Cell::getRowIndex).max().orElse(-1);

                switch (key) {
                    case "name":
                        NameColumn nameColumn = new NameColumn(maxRowIndex, columnIndex, columnCells);
                        excelHeader.addHeaderColumn(nameColumn);
                        break;

                    case "price":
                        PriceColumn priceColumn = new PriceColumn(maxRowIndex, columnIndex, columnCells);
                        excelHeader.addHeaderColumn(priceColumn);
                        break;

                    case "count":
                        CountColumn countColumn = new CountColumn(maxRowIndex, columnIndex, columnCells);
                        excelHeader.addHeaderColumn(countColumn);
                        break;
                }
            }
        }

        return excelHeader;
    }

}
