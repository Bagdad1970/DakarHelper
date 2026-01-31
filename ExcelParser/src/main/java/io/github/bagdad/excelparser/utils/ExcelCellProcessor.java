package io.github.bagdad.excelparser.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;

public class ExcelCellProcessor {

    public static boolean isCellValid(Cell cell) {
        return cell != null &&
                cell.getCellType() != CellType.BLANK &&
                cell.getCellType() != CellType.ERROR &&
                !getRawCellValue(cell).isBlank();
    }

    public static boolean isCellValueEmpty(String value) {
        return value == null || value.isBlank() || value.toLowerCase().contains("null");
    }

    public static String getRawCellValue(Cell cell) {
        if (cell == null) return "";

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
                cellValue.toLowerCase().trim()
                : "";
    }

    public static String processNameCell(Cell cell) {
        String cellValue = ExcelCellProcessor.getRawCellValue(cell);
        return ExcelCellProcessor.isCellValueEmpty(cellValue) ? null : cellValue.trim();
    }

    public static BigDecimal processPriceCell(Cell cell) {
        String cellValue = ExcelCellProcessor.getRawCellValue(cell);
        if (ExcelCellProcessor.isCellValueEmpty(cellValue)) return null;

        try {
            double value = Double.parseDouble(cellValue);
            return value > 0 ? BigDecimal.valueOf(value) : null;
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean canConvertToNumber(String cellValue) {
        if (cellValue == null || cellValue.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(cellValue.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Integer processQuantityCell(Cell cell) {
        String cellValue = ExcelCellProcessor.getRawCellValue(cell);
        if (ExcelCellProcessor.isCellValueEmpty(cellValue)) return null;

        try {
            int result;
            if (cellValue.contains(">")) {
                result = (int) Double.parseDouble(cellValue.split(">")[1]) + 1;
            }
            else {
                result = (int) Double.parseDouble(cellValue);
            }

            return Math.max(result, 0);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

}