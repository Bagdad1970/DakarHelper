package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.utils.ExcelCellProcessor;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import io.github.bagdad.excelparser.utils.HeaderParserUtils;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.ExcelHeaderCellDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import java.util.*;

@Slf4j
public class HeaderParser {

    private final ExcelHeaderCellsHandler excelHeaderCellsHandler;

    private final ParserFactory parserFactory;

    private final HeaderExtractor headerExtractor;

    @Getter
    private final Map<Category, List<Cell>> cellsGroupedByCategory;

    private final List<Cell> unprocessedHeaderCells;

    @Getter
    private final List<ExcelHeaderCellDto> unprocessableHeaderCells;

    public HeaderParser(HeaderExtractor headerExtractor, ExcelHeaderCellsHandler excelHeaderCellsHandler, ParserFactory parserFactory) {
        this.headerExtractor = headerExtractor;
        this.excelHeaderCellsHandler = excelHeaderCellsHandler;
        this.parserFactory = parserFactory;
        this.cellsGroupedByCategory = new HashMap<>();
        this.unprocessedHeaderCells = new ArrayList<>();
        this.unprocessableHeaderCells = new ArrayList<>();
    }

    public void tryToFindHeaderCells() {
        List<Row> header = headerExtractor.extractHeader();

        for (Row row : header) {
            if (row == null) {
                continue;
            }

            for (Cell cell : row) {
                String cellValue = ExcelCellProcessor.getNormalizedCellValue(cell);

                if (cellValue.isEmpty()) continue;

                CellFindStatus cellFindStatus = excelHeaderCellsHandler.getHeaderCellFindStatus(cellValue);

                if (cellFindStatus == CellFindStatus.STARTS) {
                    Category category = excelHeaderCellsHandler.getHeaderCellCategory(cellValue);
                    if (category == null) continue;

                    cellsGroupedByCategory.computeIfAbsent(category, _ -> new ArrayList<>()).add(cell);
                }
                else if (cellFindStatus == CellFindStatus.CONTAINS) {
                    ExcelHeaderCellDto excelHeaderCellDto = new ExcelHeaderCellDto();
                    excelHeaderCellDto.setOriginName(cellValue);
                    unprocessableHeaderCells.add(excelHeaderCellDto);

                    unprocessedHeaderCells.add(cell);
                }
            }
        }
    }

    public void processUnprocessedHeaderCells() {
        if (unprocessedHeaderCells.isEmpty()) {
            return;
        }

        for (Cell cell : unprocessedHeaderCells) {
            String cellValue = ExcelCellProcessor.getNormalizedCellValue(cell);

            if (cellValue.isEmpty()) continue;

            CellFindStatus cellFindStatus = excelHeaderCellsHandler.getHeaderCellFindStatus(cellValue);

            if (cellFindStatus == CellFindStatus.STARTS) {
                Category category = excelHeaderCellsHandler.getHeaderCellCategory(cellValue);

                if (category == null) continue;

                cellsGroupedByCategory.computeIfAbsent(category, _ -> new ArrayList<>()).add(cell);
            }
        }
    }

    public ExcelHeader processFoundedHeaderCells() {
        log.info("Parsing header cells");

        if (!cellsGroupedByCategory.containsKey(Category.NAME) ||
            !cellsGroupedByCategory.containsKey(Category.PRICE) ||
            !cellsGroupedByCategory.containsKey(Category.QUANTITY)
        ) {
            return null;
        }

        Map<Category, Map<Integer, List<Cell>>> cellsGroupedByColumn = HeaderParserUtils.groupByColumn(cellsGroupedByCategory);

        Map<Category, Integer> maxRowIndexForCategories = HeaderParserUtils.findMaxRowIndexInGroups(cellsGroupedByCategory);

        Map<Category, Map<Integer, List<Cell>>> cellGroupsWithMaxRow = HeaderParserUtils.removeGroupsWithoutMaxRow(maxRowIndexForCategories, cellsGroupedByColumn);

        int maxRowIndex = HeaderParserUtils.findMaxRowIndex(maxRowIndexForCategories);

        ExcelHeader excelHeader = new ExcelHeader(maxRowIndex + 1);
        for (Category category : cellGroupsWithMaxRow.keySet()) {
            var parser = parserFactory.getParserByCategory(category);
            Set<Column> headerColumns = parser.parseColumns(cellGroupsWithMaxRow.get(category));
            excelHeader.putAllHeaderColumns(category, headerColumns);
        }

        return excelHeader;
    }

}
