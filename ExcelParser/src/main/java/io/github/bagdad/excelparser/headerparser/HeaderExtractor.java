package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.utils.ExcelCellUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HeaderExtractor {

    private final Sheet sheet;

    public HeaderExtractor(Sheet sheet) {
        this.sheet = sheet;
    }

    public List<Row> extractHeader() {
        log.info("Extracting header of sheet");

        List<Row> header = new ArrayList<>();

        int i = 0;
        boolean bodyRowNotFound = true;
        while (i <= sheet.getLastRowNum() && bodyRowNotFound) {
            Row row = sheet.getRow(i);

            if (row == null) continue;

            // counter counts the presence of price and count fields
            int counterNumericValues = 0;
            for (Cell cell : row) {
                if (!ExcelCellUtils.isCellValid(cell)) {
                    continue;
                }

                if (cell.getCellType() == CellType.NUMERIC || ExcelCellUtils.canConvertToNumber(cell)) {
                    counterNumericValues++;
                }
            }

            if (counterNumericValues >= 2) {
                bodyRowNotFound = false;
            }
            else {
                header.add(row);
            }

            i++;
        }

        return header;
    }

}
