package io.github.bagdad.excelparser.headerparser.columns;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class NameColumn extends Column {

    public NameColumn(int columnIndex, String columnKey) {
        super(columnIndex, columnKey);
    }

}