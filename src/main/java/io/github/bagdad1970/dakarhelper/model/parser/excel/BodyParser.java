package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.datasource.SearchConditions;
import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.HeaderColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BodyParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(BodyParser.class);

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
        try {
            List<HeaderColumn> headerColumns = excelHeader.getHeaderColumns();
            Map<Class<? extends HeaderColumn>, List<HeaderColumn>> headerColumnsGroupedByClass = headerColumns.stream()
                    .collect(Collectors.groupingBy(HeaderColumn::getClass));

            int countHeaderColumnValid = 0;
            for (Class<? extends HeaderColumn> headerColumnClass : headerColumnsGroupedByClass.keySet()) {
                List<HeaderColumn> headerColumnsByClass = headerColumnsGroupedByClass.get(headerColumnClass);

                for (HeaderColumn headerColumn : headerColumnsByClass) {
                    int columnIndex = headerColumn.getColumnIndex();
                    Cell cell = row.getCell(columnIndex);
                    if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().contains("NULL")) {
                        countHeaderColumnValid++;
                        break;
                    }
                }
            }

            return countHeaderColumnValid == headerColumnsGroupedByClass.size();
        }
        catch (Exception exc) {
            LOGGER.error("error while validating row", exc);
        }
        return false;
    }

    public List<ExcelObject> parse(SearchConditions conditions) {
        List<ExcelObject> excelObjects = new ArrayList<>();

        int startRowIndex = getFirstValidRow();
        for (int i = startRowIndex; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if ( isRowValid(row) ) {
                ExcelObject excelObject = excelHeader.processRow(row);

                if (excelObject.validateConditions(conditions))
                    excelObjects.add(excelObject);
            }
        }
        return excelObjects;
    }

}
