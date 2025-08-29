package io.github.bagdad1970.dakarhelper.model.parser.excel.columns;

import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class NameColumn extends HeaderColumn {

    public NameColumn(int rowIndex, int columnIndex, List<Cell> cells) {
        super(rowIndex, columnIndex, cells);
    }

    @Override
    protected void initializeByCells(List<Cell> cells) {
        columnName = "name";
    }

    @Override
    public String processCellValue(Cell cell) {
        return cell.toString().trim();
    }

    @Override
    public String toString() {
        return "NameColumn{" +
                "columnIndex = " + columnIndex +
                '}';
    }

}
