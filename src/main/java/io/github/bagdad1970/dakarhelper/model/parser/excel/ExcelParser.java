package io.github.bagdad1970.dakarhelper.model.parser.excel;

import io.github.bagdad1970.dakarhelper.datasource.SearchConditions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class ExcelParser {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<String, Path> companyDirs;
    private Map<String, String> storages;
    private ObservableList<ExcelObject> excelObjects;

    private final static Map<String, String> headerNames = new LinkedHashMap<>() {{
        put("num", "№");
        put("name", "Номенклатура");
        put("price", "Цена");
        put("retail", "Розничная");
        put("wholesale", "Оптовая");
        put("internet", "Интернет");
        put("count", "Количество");
    }};

    public ExcelParser(Map<String, Path> companyDirs) {
        this.companyDirs = companyDirs;
        this.excelObjects = FXCollections.observableArrayList();
    }

    public void parseExcelFiles(SearchConditions conditions) {
        LOGGER.info("parsing excel files");

        for (String companyName : companyDirs.keySet()) {
            Path companyDir = companyDirs.get(companyName);
            File[] companyFiles = companyDir.toFile().listFiles();
            if (companyFiles != null) {
                for (File companyFile : companyFiles) {
                    parseExcelFile(companyFile, conditions);
                }
            }
        }
        //unifyExcelObjects();
        addNums();

    }

    private void addNums() {
        int excelObjectNum = 1;
        for (ExcelObject excelObject : excelObjects) {
            excelObject.addValue("num", excelObjectNum);
            excelObjectNum++;
        }
    }

    public ObservableList<ExcelObject> getExcelObjects() {
        return excelObjects;
    }

    public Map<String, String> getTableHeader() {
        if (excelObjects.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, String> tableHeader = new HashMap<>();
        Set<String> keysOfExcelObject = excelObjects.getFirst().getProps().keySet();
        for (String propKey : keysOfExcelObject) {
            if (headerNames.containsKey(propKey)) {
                String headerValue = headerNames.get(propKey);
                tableHeader.put(propKey, headerValue);
            }
        }
        //tableHeader.putAll(getStorages());

        return tableHeader;
    }

    private Map<String, String> getStorages() {
        return storages;
    }

    private void unifyExcelObjects() {
        Map<String, Integer> maxCountColumns = findMaxCountColumns();
        Set<String> maxPrices = findExcelObjectWithMaxPriceColumns();

        for (ExcelObject excelObject : excelObjects) {
            excelObject.replacePriceProps(maxPrices);
            excelObject.replaceQuantityProps(maxCountColumns);
        }
    }

    private Map<String, Integer> findMaxCountColumns() {
        int maxCountQuantityColumn = -1;
        Map<String, Integer> maxCounts = new HashMap<>();
        for (ExcelObject excelObject : excelObjects) {
            int currentCountQuantityColumn = excelObject.getCounts().size();
            if (currentCountQuantityColumn > maxCountQuantityColumn) {
                maxCountQuantityColumn = currentCountQuantityColumn;
                maxCounts.putAll(excelObject.getCounts());
            }
        }
        return maxCounts;
    }

    private Set<String> findExcelObjectWithMaxPriceColumns() {
        int maxCountPriceColumn = -1;
        Set<String> prices = new HashSet<>();
        for (ExcelObject excelObject : excelObjects) {
            int currentCountPriceColumn = excelObject.getPrices().size();
            if (currentCountPriceColumn > maxCountPriceColumn) {
                maxCountPriceColumn = currentCountPriceColumn;
                prices.addAll( excelObject.getPrices().keySet() );
            }
        }
        return prices;
    }

    private Workbook loadWorkbook(File companyFile) {
        Workbook workbook = null;
        try (FileInputStream excelFileStream = new FileInputStream(companyFile)) {
            if (companyFile.getName().endsWith(".xls")) {
                workbook = new HSSFWorkbook(excelFileStream);
            }
            else if (companyFile.getName().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(excelFileStream);
            }
            else {
                LOGGER.warn("Unsupported file format: {}", companyFile.getName());
                return null;
            }
        }
        catch (FileNotFoundException exc) {
            LOGGER.error("file not found", exc);
        }
        catch (Exception exc) {
            LOGGER.error("fatal", exc);
        }
        return workbook;
    }

    private void parseExcelFile(File companyFile, SearchConditions conditions) {
        LOGGER.info("parsing excel file {}", companyFile);

        if (companyFile == null || !companyFile.exists() || !companyFile.isFile()) {
            LOGGER.error("File does not exist or is not a file: {}",
                    companyFile != null ? companyFile.getAbsolutePath() : "null");
            return;
        }

        Workbook workbook = loadWorkbook(companyFile);
        if (workbook == null) {
            LOGGER.error("Failed to load workbook from file: {}", companyFile.getAbsolutePath());
            return;
        }

        if (workbook.getNumberOfSheets() == 0) {
            LOGGER.error("Workbook contains no sheets");
            closeWorkbook(workbook);
            return;
        }

        Sheet sheet = workbook.getSheetAt(0);

        if (sheet == null || sheet.getLastRowNum() <= 0) {
            LOGGER.warn("Sheet is empty or contains no data");
            closeWorkbook(workbook);
            return;
        }

        try {
            HeaderParser headerParser = new HeaderParser(sheet, 12);

            ExcelHeader excelHeader = headerParser.parseHeader();

            BodyParser bodyParser = new BodyParser(sheet, excelHeader);

            List<ExcelObject> parsedExcelObjects = bodyParser.parse(conditions);
            if ( !parsedExcelObjects.isEmpty() ) {
                excelObjects.addAll(parsedExcelObjects);
                replaceStorages(excelHeader.getStorages());
            }
        }
        catch (Exception exc) {
            LOGGER.error("Error parsing Excel file: {}", companyFile.getAbsolutePath(), exc);
        }
        finally {
            closeWorkbook(workbook);
        }
    }

    private void replaceStorages(Map<String, String> newStorages) {
        if (storages == null || storages.isEmpty() || storages.size() < newStorages.size()) {
            storages = newStorages;
        }
    }

    private void closeWorkbook(Workbook workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            }
            catch (IOException exc) {
                LOGGER.error("Error closing workbook", exc);
            }
        }
    }

}
