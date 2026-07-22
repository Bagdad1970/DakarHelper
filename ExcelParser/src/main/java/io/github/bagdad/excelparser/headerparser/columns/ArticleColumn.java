package io.github.bagdad.excelparser.headerparser.columns;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ArticleColumn extends Column {

    public ArticleColumn(int columnIndex, String columnKey) {
        super(columnIndex, columnKey);
    }

}