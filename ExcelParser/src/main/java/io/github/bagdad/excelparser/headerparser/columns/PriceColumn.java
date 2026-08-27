package io.github.bagdad.excelparser.headerparser.columns;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PriceColumn extends CategoryColumn {

    public PriceColumn(int columnIndex, String columnKey) {
        super(columnIndex, columnKey);
    }

}
