package io.github.bagdad1970.dakarhelper.model.parser.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;


public class ExcelParser {

    private static final Logger LOGGER = LogManager.getLogger();

    private Sheet sheet;

    public ExcelParser(Sheet sheet) {
        this.sheet = sheet;
    }

    public List<ExcelRow> parse() {
        LOGGER.info("parsing excel file");

        HeaderParser headerParser = new HeaderParser(sheet, 10);

        ExcelHeader excelHeader = headerParser.parseHeader();

        BodyParser bodyParser = new BodyParser(sheet, excelHeader);
        bodyParser.parse();

        return bodyParser.getItems();
    }


}
