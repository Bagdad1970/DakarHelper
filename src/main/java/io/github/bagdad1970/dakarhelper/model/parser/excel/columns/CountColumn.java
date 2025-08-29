package io.github.bagdad1970.dakarhelper.model.parser.excel.columns;

import io.github.bagdad1970.dakarhelper.model.parser.excel.Aliases;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Arrays;
import java.util.List;

public class CountColumn extends HeaderColumn {

    private String storageName;

    private static Aliases aliases = new Aliases() {{
        addAliases("storage", Arrays.asList("склад"));
    }};

    public CountColumn(int rowIndex, int columnIndex, List<Cell> cells) {
        super(rowIndex, columnIndex, cells);
    }

    @Override
    protected void initializeByCells(List<Cell> cells) {
        columnName = "count";

        if ( !cells.isEmpty() ) {
            for (Cell cell : cells) {
                String cellValue = cell.toString();
                storageName = aliases.getKeyByAlias(cellValue);
            }
        }
    }

    @Override
    public Object processCellValue(Cell cell) {
        String cellValue = cell.toString().trim();

        return (int) Double.parseDouble(cellValue);
    }

    @Override
    public String toString() {
        return "CountColumn{" +
                "columnIndex = " + columnIndex +
                '}';
    }

}
