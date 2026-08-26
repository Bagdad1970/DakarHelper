package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class CategoryParser {

    protected final SubcategoryMapping subcategoryMapping;

    public CategoryParser(SubcategoryMapping subcategoryMapping) {
        this.subcategoryMapping = subcategoryMapping;
    }

    public abstract Set<Column> parseColumns(Map<Integer, List<Cell>> cellsByClass);

}
