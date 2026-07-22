package io.github.bagdad.excelparser.headerparser.columns;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class PriceColumn extends Column {

    public PriceColumn(int columnIndex, String columnKey) {
        super(columnIndex, columnKey);
    }

}
