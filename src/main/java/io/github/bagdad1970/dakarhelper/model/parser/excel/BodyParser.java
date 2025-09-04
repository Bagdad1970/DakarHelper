package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.datasource.SearchConditions;
import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.HeaderColumn;
import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.QuantityColumn;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BodyParser {

    private final Sheet sheet;
    private final ExcelHeader excelHeader;

    public BodyParser(Sheet sheet, ExcelHeader excelHeader) {
        this.sheet = sheet;
        this.excelHeader = excelHeader;
    }

    public int getFirstValidRow() {
        int startRowIndex = excelHeader.getStartRowIndex() + 1;
        for (int i = startRowIndex; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if ( isRowValid(row) ) {
                return i;
            }
        }
        return -1;
    }

    private boolean isRowValid(Row row) {
        List<HeaderColumn> headerColumns = excelHeader.getHeaderColumns();
        Map<Class<? extends HeaderColumn>, List<HeaderColumn>> headerColumnsGroupedByClass = headerColumns.stream()
                .collect(Collectors.groupingBy(HeaderColumn::getClass));

        int countHeaderColumnValid = 0;
        for (Class<? extends HeaderColumn> headerColumnClass : headerColumnsGroupedByClass.keySet()) {
            List<HeaderColumn> headerColumnsByClass = headerColumnsGroupedByClass.get(headerColumnClass);

            for (HeaderColumn headerColumn : headerColumnsByClass) {
                int columnIndex = headerColumn.getColumnIndex();
                Cell cell = row.getCell(columnIndex);
                if ( cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().contains("NULL") ) {
                    countHeaderColumnValid++;
                    break;
                }
            }
        }

        return countHeaderColumnValid == headerColumnsGroupedByClass.size();
    }

    public List<ExcelObject> parse(SearchConditions conditions) {
        List<ExcelObject> excelObjects = new ArrayList<>();

        int startRowIndex = getFirstValidRow();
        for (int i = startRowIndex; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if ( isRowValid(row) ) {
                ExcelObject excelObject = excelHeader.processRow(row);

                if (excelObject.validateConditions(conditions)) {
                    excelObjects.add(excelObject);
                }
            }
        }
        return excelObjects;
    }

}
