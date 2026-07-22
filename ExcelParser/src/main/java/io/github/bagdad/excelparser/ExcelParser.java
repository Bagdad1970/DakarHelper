package io.github.bagdad.excelparser;

import io.github.bagdad.excelparser.bodyparser.BodyParser;
import io.github.bagdad.excelparser.model.ExcelProduct;
import io.github.bagdad.excelparser.headerparser.ExcelHeader;
import io.github.bagdad.excelparser.headerparser.HeaderParser;
import io.github.bagdad.excelparser.headerparser.ParserFactory;
import io.github.bagdad.excelparser.utils.ExcelHeaderCellsHandler;
import io.github.bagdad.models.excelparser.HeaderCellDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

@Slf4j
public class ExcelParser {

    private final Sheet sheet;

    private final HeaderParser headerParser;

    public ExcelParser(Sheet sheet, ExcelHeaderCellsHandler excelHeaderCellsHandler, ParserFactory parserFactory) {
        this.sheet = sheet;
        this.headerParser = new HeaderParser(sheet, excelHeaderCellsHandler, parserFactory);
    }

    public List<HeaderCellDto> getUnprocessableHeaderCells() {
        return headerParser.getUnprocessableHeaderCells();
    }

    public boolean tryToParse() {
        log.info("Trying to parse excel sheet");

        headerParser.tryToFindHeaderCells();

        return !headerParser.getUnprocessableHeaderCells().isEmpty();
    }

    public void processUnprocessedCells() {
        headerParser.processUnprocessedHeaderCells();
    }

    public List<ExcelProduct> parse() {
        ExcelHeader excelHeader = headerParser.processFoundHeaderCells();

        BodyParser bodyParser = new BodyParser(sheet, excelHeader);

        return bodyParser.parse();
    }

}
