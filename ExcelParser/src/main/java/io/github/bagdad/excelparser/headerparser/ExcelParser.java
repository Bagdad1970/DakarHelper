package io.github.bagdad.excelparser.headerparser;

import io.github.bagdad.excelparser.headerparser.bodyparser.BodyParser;
import io.github.bagdad.excelparser.headerparser.headerparser.HeaderExtractor;
import io.github.bagdad.excelparser.headerparser.model.ExcelProduct;
import io.github.bagdad.excelparser.headerparser.headerparser.ExcelHeader;
import io.github.bagdad.excelparser.headerparser.headerparser.HeaderParser;
import io.github.bagdad.excelparser.headerparser.headerparser.ParserFactory;
import io.github.bagdad.excelparser.headerparser.utils.ExcelHeaderCellsHandler;
import io.github.bagdad.excelparser.headerparser.utils.ExcelWorkbookHandler;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelParser {

    private final Sheet sheet;

    private final HeaderParser headerParser;

    private ExcelHeader excelHeader;

    public ExcelParser(String filepath, ExcelHeaderCellsHandler excelHeaderCellsHandler, ParserFactory parserFactory) {
        this.sheet = getFirstSheetFromFile(filepath);

        HeaderExtractor headerExtractor = new HeaderExtractor(sheet);

        this.headerParser = new HeaderParser(headerExtractor, excelHeaderCellsHandler, parserFactory);
    }

    public List<HeaderCellDto> getUnprocessableHeaderCells() {
        return headerParser.getUnprocessableHeaderCells();
    }

    public Map<String, String> getStorages() {
        return excelHeader.getStorages();
    }

    private Sheet getFirstSheetFromFile(String filepath) {
        Workbook workbook = ExcelWorkbookHandler.loadWorkbook(filepath);

        if (workbook == null) {
            return null;
        }

        return ExcelWorkbookHandler.getFirstSheet(workbook);
    }

    public boolean tryToParse() {
        headerParser.tryToFindHeaderCells();

        return !headerParser.getUnprocessableHeaderCells().isEmpty();
    }

    public void processUnprocessedCells() {
        headerParser.processUnprocessedHeaderCells();
    }

    public List<ExcelProduct> parse() {
        excelHeader = headerParser.processFoundedHeaderCells();

        BodyParser bodyParser = new BodyParser(sheet, excelHeader);

        return bodyParser.parse();
    }

}
