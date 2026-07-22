package io.github.bagdad.excelparser.headerparser.columns;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class Column {

    protected final int columnIndex;

    protected String columnKey;

    protected Column(int columnIndex, String columnKey) {
        this.columnIndex = columnIndex;
        this.columnKey = columnKey;
    }

}