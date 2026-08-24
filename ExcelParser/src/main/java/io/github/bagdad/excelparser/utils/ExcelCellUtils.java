package io.github.bagdad.excelparser.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;

public class ExcelCellUtils {

    private static final String NULL_CELL_VALUE = "null";

    public static boolean isCellValid(Cell cell) {
        return cell != null &&
                cell.getCellType() != CellType.BLANK &&
                cell.getCellType() != CellType.ERROR &&
                !getRawCellValue(cell).isBlank();
    }

    public static boolean isCellValueEmpty(String value) {
        return value == null ||
                value.isBlank() ||
                value.toLowerCase().contains(NULL_CELL_VALUE);
    }

    public static String getRawCellValue(Cell cell) {
        if (cell == null)
            return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                double d = cell.getNumericCellValue();
                yield String.valueOf(d);
            }
            default -> "";
        };
    }

    public static String getNormalizedCellValue(Cell cell) {
        String cellValue = getRawCellValue(cell);
        return cellValue != null ?
                cellValue.trim().toLowerCase()
                : "";
    }

    public static boolean canConvertToNumber(Cell cell) {
        String cellValue = getNormalizedCellValue(cell);

        if (cellValue.isBlank())
            return false;

        try {
            Double.parseDouble(cellValue);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

}