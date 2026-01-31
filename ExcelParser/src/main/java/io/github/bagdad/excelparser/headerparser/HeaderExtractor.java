package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.utils.ExcelCellProcessor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

public class HeaderExtractor {

    private final Sheet sheet;

    public HeaderExtractor(Sheet sheet) {
        this.sheet = sheet;
    }

    public List<Row> extractHeader() {
        List<Row> header = new ArrayList<>();

        int i = 0;
        boolean bodyRowNotFounded = true;
        while (i <= sheet.getLastRowNum() && bodyRowNotFounded) {
            Row row = sheet.getRow(i);

            if (row != null) {
                int counterNumberValues = 0;
                for (Cell cell : row) {
                    if (!ExcelCellProcessor.isCellValid(cell)) {
                        continue;
                    }

                    String cellValue = ExcelCellProcessor.getRawCellValue(cell);

                    if (cell.getCellType() == CellType.NUMERIC || ExcelCellProcessor.canConvertToNumber(cellValue)) {
                        counterNumberValues++;
                    }
                }

                if (counterNumberValues >= 2) {
                    bodyRowNotFounded = false;
                }
                else {
                    header.add(row);
                }
            }

            i++;
        }

        return header;
    }

}
