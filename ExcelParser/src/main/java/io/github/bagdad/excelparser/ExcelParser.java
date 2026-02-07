package io.github.bagdad.excelparser;

import io.github.bagdad.excelparser.bodyparser.BodyParser;
import io.github.bagdad.excelparser.headerparser.HeaderExtractor;
import io.github.bagdad.excelparser.model.ExcelProduct;
import io.github.bagdad.excelparser.headerparser.ExcelHeader;
import io.github.bagdad.excelparser.headerparser.HeaderParser;
import io.github.bagdad.excelparser.headerparser.ParserFactory;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import io.github.bagdad.excelparser.utils.ExcelWorkbookHandler;
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
