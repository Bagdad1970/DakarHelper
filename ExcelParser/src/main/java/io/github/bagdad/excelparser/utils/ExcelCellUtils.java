package io.github.bagdad.excelparser.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;

public class ExcelCellUtils {

    private static final String NULL_CELL_VALUE = "null";

    private static final String MORE_QUANTITY_WORD = "более";
    private static final String MORE_QUANTITY_SYMBOL = ">";

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

    public static String processArticleCell(Cell cell) {
        String cellValue = ExcelCellUtils.getRawCellValue(cell);

        return ExcelCellUtils.isCellValueEmpty(cellValue) ? null : cellValue.trim();
    }

    public static String processNameCell(Cell cell) {
        String cellValue = ExcelCellUtils.getRawCellValue(cell);

        return ExcelCellUtils.isCellValueEmpty(cellValue) ? null : cellValue.trim();
    }

    public static BigDecimal processPriceCell(Cell cell) {
        String cellValue = ExcelCellUtils.getRawCellValue(cell);
        if (ExcelCellUtils.isCellValueEmpty(cellValue)) return null;

        try {
            double value = Double.parseDouble(cellValue);
            return value > 0 ? BigDecimal.valueOf(value) : null;
        }
        catch (NumberFormatException e) {
            return null;
        }
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

    public static Integer processQuantityCell(Cell cell) {
        String cellValue = ExcelCellUtils.getNormalizedCellValue(cell);
        if (ExcelCellUtils.isCellValueEmpty(cellValue))
            return null;

        try {
            int result;

            // what if will be "1>23". it is 123 or 23?
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

}