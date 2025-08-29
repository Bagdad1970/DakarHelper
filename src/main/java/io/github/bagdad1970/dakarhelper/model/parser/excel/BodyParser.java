package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.CountColumn;
import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.HeaderColumn;
import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.NameColumn;
import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.PriceColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BodyParser {

    private Sheet sheet;
    private ExcelHeader excelHeader;
    private List<ExcelRow> items;

    public BodyParser(Sheet sheet, ExcelHeader excelHeader) {
        this.sheet = sheet;
        this.excelHeader = excelHeader;
        this.items = new ArrayList<>();
    }

    public void parse() {
        int maxRowIndex = excelHeader.getStartRowIndex();

        for (int i = maxRowIndex + 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            boolean isExcelRow = true;
            for (HeaderColumn headerColumn : excelHeader.getHeaderColumns()) {
                int columnIndex = headerColumn.getColumnIndex();

                Cell cell = row.getCell(columnIndex);

                if (cell == null || cell.getCellType() == CellType.BLANK) {
                    isExcelRow = false;
                    break;
                } // проверка сложнее т.к. может быть количесттво в резерве пустым

            }

            if ( !isExcelRow ) continue;

            items.add(excelHeader.processRow(row));
        }
    }

    public List<ExcelRow> getItems() {
        return items;
    }

}
