package io.github.bagdad1970.dakarhelper.model.parser.excel.columns;

import io.github.bagdad1970.dakarhelper.model.parser.excel.Aliases;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PriceColumn extends HeaderColumn {

    private static Aliases aliases = new Aliases() {{
        addAliases("wholesale", Arrays.asList("опт", "оптовый", "оптовая"));
        addAliases("retail", Arrays.asList("розничный", "розница", "розничная"));
        addAliases("internet", Arrays.asList("интернет"));
    }};

    public PriceColumn(int rowIndex, int columnIndex, List<Cell> cells) {
        super(rowIndex, columnIndex, cells);
    }

    @Override
    protected void initializeByCells(List<Cell> cells) {
        if (cells.isEmpty()) {
            return;
        }

        String key = "";
        for (Cell cell : cells) {
            String cellValue = cell.toString();
            key = aliases.getKeyByAlias(cellValue);
        }

        columnName = switch (key) {
            case "retail" -> "retail";
            case "wholesale" -> "wholesale";
            case "internet" -> "internet";
            default -> "price";
        };
    }

    @Override
    public Object processCellValue(Cell cell) {
        String cellValue = cell.toString().trim();

        if ( cellValue.isEmpty() ) {
            return null;
        }

        return Double.parseDouble(cellValue);
    }

    @Override
    public String toString() {
        return "PriceColumn{" +
                "columnIndex = " + columnIndex + ", " +
                "columnName = " + columnName +
                '}';
    }
}
