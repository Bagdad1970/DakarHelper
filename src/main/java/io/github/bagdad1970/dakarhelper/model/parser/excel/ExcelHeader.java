package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.QuantityColumn;
import io.github.bagdad1970.dakarhelper.model.parser.excel.columns.HeaderColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelHeader {

    private final List<HeaderColumn> headerColumns = new ArrayList<>();

    public ExcelObject processRow(Row row) {
        ExcelObject excelObject = new ExcelObject();

        for (HeaderColumn headerColumn : headerColumns) {
            int columnIndex = headerColumn.getColumnIndex();
            String key = headerColumn.getColumnName();

            Cell cell = row.getCell(columnIndex);

            Object value = headerColumn.processCellValue(cell);

            excelObject.addProp(key, value);
        }

        return excelObject;
    }

    public Map<String, String> getStorages() {
        List<QuantityColumn> quantityColumns = headerColumns.stream()
                .filter(quanity -> quanity instanceof QuantityColumn)
                .map(quantity -> (QuantityColumn) quantity)
                .toList();

        return quantityColumns.stream()
                .collect(Collectors.toMap(
                        QuantityColumn::getStorageColumnName,
                        QuantityColumn::getStorageName
                ));
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
