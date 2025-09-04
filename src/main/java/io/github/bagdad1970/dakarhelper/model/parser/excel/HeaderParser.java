package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.QuantityColumn;
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
        addAliases("price", Arrays.asList("цена", "стоимость", "опт", "розница", "розничная", "интернет"));
        addAliases("count", Arrays.asList("остаток", "количество", "склад"));
    }};
    private final Sheet sheet;
    private final int limitRowCount;

    public HeaderParser(Sheet sheet, int limitRowCount) {
        this.sheet = sheet;
        this.limitRowCount = limitRowCount;
    }

    public Map<String, List<Cell>> findHeaderCells() {
        Map<String, List<Cell>> headerCellsByAlias = new HashMap<>();

        for (int i = 0; i < limitRowCount; i++) {
            Row row = sheet.getRow(i);

            for (Cell cell : row) {
                String cellValue = cell.toString();

                for (String key : aliases.keySet()) {
                    if (aliases.hasAliasByKey(key, cellValue)) {
                        if ( !headerCellsByAlias.containsKey(key) ) {
                            List<Cell> cells = new ArrayList<>();
                            cells.add(cell);
                            headerCellsByAlias.put(key, cells);
                        }
                        else
                            headerCellsByAlias.get(key).add(cell);

                        break;
                    }
                }
            }
        }

        Map<String, List<Cell>> clearedIndexMap = new HashMap<>();
        for (String key : headerCellsByAlias.keySet()) {
            List<Cell> cellsOnMaxRow = findCellsOnMaxRow(headerCellsByAlias.get(key));
            clearedIndexMap.put(key, cellsOnMaxRow);
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

    static int findMaxRowIndex(List<Cell> cells) {
        return cells.stream()
                .mapToInt(Cell::getRowIndex)
                .max()
                .orElse(-1);
    }

    static List<Cell> findCellsOnMaxRow(List<Cell> cells) {
        if (cells.isEmpty())
            return new ArrayList<>();

        else if (cells.size() == 1)
            return cells;

        int maxRowIndex = findMaxRowIndex(cells);

        List<Cell> cellsOnNonMaxRow = getCellsWithNonMaxRow(maxRowIndex, cells);

        List<Integer> columnIndexesOnMaxRow = getColumnIndexesOnRow(maxRowIndex, cells);

        List<Cell> cellsOnMaxRow = new ArrayList<>(getCellsOnRow(maxRowIndex, cells));

        for (Cell cell : cellsOnNonMaxRow) {
            int cellColumnIndex = cell.getColumnIndex();
            if (columnIndexesOnMaxRow.contains(cellColumnIndex))
                cellsOnMaxRow.add(cell);
        }

        return cellsOnMaxRow;
    }

    static List<Cell> getCellsWithNonMaxRow(int rowIndex, List<Cell> cells) {
        return cells.stream()
                .filter(cell -> cell.getRowIndex() != rowIndex)
                .toList();
    }

    static List<Integer> getColumnIndexesOnRow(int rowIndex, List<Cell> cells) {
        return cells.stream()
                .filter(cell -> cell.getRowIndex() == rowIndex)
                .map(Cell::getColumnIndex)
                .toList();
    }

    static List<Cell> getCellsOnRow(int rowIndex, List<Cell> cells) {
        return cells.stream()
                .filter(cell -> cell.getRowIndex() == rowIndex)
                .toList();
    }

    public ExcelHeader parseHeader() {
        Map<String, List<Cell>> headerCells = findHeaderCells();

        Map<String, Map<Integer, List<Cell>>> groupedCellsByColumnIndex = HeaderParser.groupByColumns(headerCells);

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
                        QuantityColumn quantityColumn = new QuantityColumn(maxRowIndex, columnIndex, columnCells);
                        excelHeader.addHeaderColumn(quantityColumn);
                        break;
                }
            }
        }

        return excelHeader;
    }

}
