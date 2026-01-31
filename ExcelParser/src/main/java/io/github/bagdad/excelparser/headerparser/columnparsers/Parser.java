package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.columns.Column;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Parser {

    Set<Column> parseColumns(Map<Integer, List<Cell>> cellsByClass);

}
