package io.github.bagdad.excelparser.headerparser.columns;

import io.github.bagdad.excelparser.headerparser.Storage;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class QuantityColumn extends Column {

    @EqualsAndHashCode.Include
    private final Storage storage;

    public QuantityColumn(int columnIndex, String columnKey, Storage storage) {
        super(columnIndex, columnKey);

        this.storage = storage;
    }

}