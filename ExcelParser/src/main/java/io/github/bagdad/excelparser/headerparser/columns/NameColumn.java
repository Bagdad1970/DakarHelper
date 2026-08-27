package io.github.bagdad.excelparser.headerparser.columns;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NameColumn extends CategoryColumn {

    public NameColumn(int columnIndex, String columnKey) {
        super(columnIndex, columnKey);
    }

}