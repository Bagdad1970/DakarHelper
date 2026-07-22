package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.headerparser.columns.Column;
import io.github.bagdad.excelparser.model.CellFindStatus;
import io.github.bagdad.excelparser.utils.ExcelCellUtils;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import io.github.bagdad.excelparser.utils.HeaderParserUtils;
import io.github.bagdad.models.excelparser.Category;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HeaderParser {

    private final ExcelHeaderCellsHandler excelHeaderCellsHandler;

    private final ParserFactory parserFactory;

    private final HeaderExtractor headerExtractor;

    @Getter
    private final Map<Category, List<Cell>> cellsGroupedByCategory;

    private final List<Cell> unprocessedHeaderCells;

    @Getter
    private final List<HeaderCellDto> unprocessableHeaderCells;

    public HeaderParser(Sheet sheet, ExcelHeaderCellsHandler excelHeaderCellsHandler, ParserFactory parserFactory) {
        this.headerExtractor = new HeaderExtractor(sheet);
        this.excelHeaderCellsHandler = excelHeaderCellsHandler;
        this.parserFactory = parserFactory;
        this.cellsGroupedByCategory = new HashMap<>();
        this.unprocessedHeaderCells = new ArrayList<>();
        this.unprocessableHeaderCells = new ArrayList<>();
    }

    public void tryToFindHeaderCells() {
        List<Row> header = headerExtractor.extractHeader();

        for (Row row : header) {
            for (Cell cell : row) {
                String cellValue = ExcelCellUtils.getNormalizedCellValue(cell);

                if (cellValue.isEmpty()) continue;

                CellFindStatus cellFindStatus = excelHeaderCellsHandler.findHeaderCellFindStatus(cellValue);

                switch (cellFindStatus) {
                    case STARTS -> {
                        Category category = excelHeaderCellsHandler.findHeaderCellCategory(cellValue);
                        if (category != null) {
                            cellsGroupedByCategory.computeIfAbsent(category, _ -> new ArrayList<>()).add(cell);
                        }
                    }

                    case CONTAINS -> {
                        HeaderCellDto headerCellDto = HeaderCellDto.builder()
                                .originalName(cellValue)
                                .build();

                        unprocessableHeaderCells.add(headerCellDto);
                        unprocessedHeaderCells.add(cell);
                    }
                }
            }
        }
    }

    public void processUnprocessedHeaderCells() {
        if (unprocessedHeaderCells.isEmpty()) {
            return;
        }

        for (Cell cell : unprocessedHeaderCells) {
            String cellValue = ExcelCellUtils.getNormalizedCellValue(cell);

            if (cellValue.isEmpty()) continue;

            CellFindStatus cellFindStatus = excelHeaderCellsHandler.findHeaderCellFindStatus(cellValue);

            if (cellFindStatus == CellFindStatus.STARTS) {
                Category category = excelHeaderCellsHandler.findHeaderCellCategory(cellValue);
                if (category != null) {
                    cellsGroupedByCategory.computeIfAbsent(category, _ -> new ArrayList<>()).add(cell);
                }
            }
        }
    }

    public ExcelHeader processFoundHeaderCells() {
        log.info("Parsing found header cells");

        if (!HeaderParserUtils.isHeaderValid(cellsGroupedByCategory)) {
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
