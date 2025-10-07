package io.github.bagdad1970.dakarhelper.model.parser.excel.columns;

import io.github.bagdad1970.dakarhelper.utils.Aliases;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuantityColumn extends HeaderColumn {

    private String storageName;
    private String storageKey;
    private static Aliases aliases = new Aliases() {{
        addAliases("extra-storage", Arrays.asList("резервный склад", "ожидаемый"));
        addAliases("main-storage", Arrays.asList("основной склад"));
        addAliases("count", Arrays.asList("остаток", "количество"));
    }};

    public QuantityColumn(int rowIndex, int columnIndex, List<Cell> cells) {
        super(rowIndex, columnIndex, cells);
    }

    public String getStorageName() {
        return storageName;
    }

    public String getStorageKey() {
        return storageKey;
    }

    @Override
    protected void initializeByCells(List<Cell> cells) {
        if (cells.isEmpty()) {
            return;
        }

        Pattern storagePattern = Pattern.compile("склад \\d+");

        for (Cell cell : cells) {
            String cellValue = cell.toString().toLowerCase().trim();

            Matcher matcher = storagePattern.matcher(cellValue);
            if (matcher.find()) {
                String foundedSubstring = cellValue.substring(matcher.start(), matcher.end());
                int storageNumber = Integer.parseInt(foundedSubstring.split(" ")[1]);

                storageName = "Склад " + storageNumber;
                storageKey = "storage" + storageNumber;
                columnName = "count" + storageNumber;
            }
            else {
                if (aliases.hasAliasByKey("extra-storage", cellValue)) {
                    storageName = "Резервный склад";
                    storageKey = "storage2";
                    columnName = "count2";
                }
                else if (aliases.hasAliasByKey("main-storage", cellValue)) {
                    storageName = "Основной склад";
                    storageKey = "storage1";
                    columnName = "count1";
                }
                else if (aliases.hasAliasByKey("count", cellValue)) {
                    columnName = "count1";
                    storageName = "Основной склад";
                    storageKey = "storage1";
                }
            }
        }
    }

    @Override
    public Object processCellValue(Cell cell) {
        String cellValue = cell.toString().trim();

        if ( isCellEmpty(cellValue) ) {
            return null;
        }

        if (cellValue.contains("более")) {
            String countValue = cellValue.split(" ")[1];
            return (int) Double.parseDouble(countValue) + 1;
        }
        else if (cellValue.contains("менее")) {
            String countValue = cellValue.split(" ")[1];
            return (int) Double.parseDouble(countValue) - 1;
        }

        return (int) Double.parseDouble(cellValue);
    }

    @Override
    public String toString() {
        return "QuantityColumn{" +
                "columnIndex = " + columnIndex +
                "columnName = " + columnName +
                "storageName = " + storageName +
                '}';
    }

}
