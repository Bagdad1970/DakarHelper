package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.HeaderColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

public class ExcelHeader {

    private List<HeaderColumn> headerColumns = new ArrayList<>();

    public ExcelRow processRow(Row row) {
        ExcelRow excelRow = new ExcelRow();

        for (HeaderColumn headerColumn : headerColumns) {
            int columnIndex = headerColumn.getColumnIndex();
            String key = headerColumn.getColumnName();

            Cell cell = row.getCell(columnIndex);

            Object value = headerColumn.processCellValue(cell);

            excelRow.addValue(key, value);
        }

        return excelRow;
    }

    public void addHeaderColumn(HeaderColumn headerColumn) {
        this.headerColumns.add(headerColumn);
    }

    public int getStartRowIndex() {
        return headerColumns.stream()
                .mapToInt(HeaderColumn::getRowIndex)
                .max()
                .orElse(-1);
    }

    public List<HeaderColumn> getHeaderColumns() {
        return headerColumns;
    }

}
