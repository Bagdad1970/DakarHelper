package io.github.bagdad1970.dakarhelper.model.parser.excel.columns;

import org.apache.poi.ss.usermodel.Cell;
import java.util.List;

public abstract class HeaderColumn {

    protected final int columnIndex;
    protected final int rowIndex;
    protected String columnName;

    protected HeaderColumn(int rowIndex, int columnIndex, List<Cell> cells) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        initializeByCells(cells);
    }

    protected abstract void initializeByCells(List<Cell> cells);

    public abstract Object processCellValue(Cell cell);

    protected boolean isCellEmpty(String cellValue) {
        return cellValue.isEmpty() || cellValue.toLowerCase().contains("null");
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public String toString() {
        return "PriceColumn{" +
                "rowIndex = " + rowIndex + ", " +
                "columnIndex = " + columnIndex +
                '}';
    }

}
