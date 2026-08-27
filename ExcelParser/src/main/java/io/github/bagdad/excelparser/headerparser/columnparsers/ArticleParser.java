package io.github.bagdad.excelparser.headerparser.columnparsers;

import io.github.bagdad.excelparser.headerparser.columns.ArticleColumn;
import io.github.bagdad.excelparser.headerparser.columns.CategoryColumn;
import io.github.bagdad.excelparser.utils.ExcelCellUtils;
import io.github.bagdad.excelparser.utils.SubcategoryMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ArticleParser extends CategoryParser {

    public ArticleParser(SubcategoryMapping subcategoryMapping) {
        super(subcategoryMapping);
    }

    public static String processCell(Cell cell) {
        String cellValue = ExcelCellUtils.getRawCellValue(cell);

        if (ExcelCellUtils.isCellValueEmpty(cellValue))
            return null;

        return cellValue.trim();
    }

    @Override
    public Set<CategoryColumn> parseColumns(Map<Integer, List<Cell>> cellsByClass) {
        log.info("Parsing article columns");

        if (cellsByClass.isEmpty()) {
            return Collections.emptySet();
        }

        Set<CategoryColumn> columns = new HashSet<>();
        for (int columnIndex : cellsByClass.keySet()) {
            List<Cell> columnCells = cellsByClass.get(columnIndex);

            for (Cell cell : columnCells) {
                String cellValue = ExcelCellUtils.getNormalizedCellValue(cell);
                String columnName = subcategoryMapping.getKeyByValue(cellValue);
                if (columnName != null) {
                    columns.add(new ArticleColumn(columnIndex, columnName));
                }
            }
        }

        return columns;
    }

}
