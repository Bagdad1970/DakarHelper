package io.github.bagdad.excelparser.headerparser.columns;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public abstract class CategoryColumn {

    protected final int columnIndex;

    protected String columnKey;

    protected CategoryColumn(int columnIndex, String columnKey) {
        this.columnIndex = columnIndex;
        this.columnKey = columnKey;
    }

}