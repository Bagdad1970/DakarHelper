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

    private static final Logger LOGGER = LogManager.getLogger(ExcelParser.class);

    private final Map<String, Path> companyDirs;
    private Map<String, String> storages;
    private ObservableList<ExcelObject> excelObjects;

    private final static Map<String, String> headerNames = new HashMap<>() {{
        put("num", "№");
        put("name", "Номенклатура");
        put("price", "Цена");
        put("retail", "Розничная");
        put("wholesale", "Оптовая");
        put("internet", "Интернет");
        put("count", "Количество");
        put("company", "Компания");
    }};

    public ExcelParser(Map<String, Path> companyDirs) {
        this.companyDirs = companyDirs;
        this.excelObjects = FXCollections.observableArrayList();
    }

    public void parseExcelFiles(SearchConditions conditions) {
        LOGGER.info("parsing excel files");

        Set<String> maxQuantityKeys = new HashSet<>();
        Set<String> maxPriceKeys = new HashSet<>();

        for (String companyName : companyDirs.keySet()) {
            Path companyDir = companyDirs.get(companyName);
            File[] companyFiles = companyDir.toFile().listFiles();
            if (companyFiles != null) {
                for (File companyFile : companyFiles) {
                    List<ExcelObject> excelObjectsInFile = parseExcelFile(companyFile, conditions);
                    addCompanyName(excelObjectsInFile, companyName);
                    excelObjects.addAll(excelObjectsInFile);

                    if ( !excelObjectsInFile.isEmpty() ) {
                        Set<String> currentPriceKeys = excelObjectsInFile.getFirst().getPriceKeys();
                        if (currentPriceKeys.size() > maxPriceKeys.size())
                            maxPriceKeys = currentPriceKeys;

                        Set<String> currentQuantityKeys = excelObjectsInFile.getFirst().getQuantityKeys();
                        if (currentQuantityKeys.size() > maxQuantityKeys.size())
                            maxQuantityKeys = currentQuantityKeys;
                    }
                }
            }
        }
        addNums();
        unifyExcelObjects(maxQuantityKeys, maxPriceKeys);
    }

    private void addNums() {
        LOGGER.info("adding nums to excel objects");

        int excelObjectNum = 1;
        for (ExcelObject excelObject : excelObjects) {
            excelObject.addProp("num", excelObjectNum);
            excelObjectNum++;
        }
    }

    private static void addCompanyName(List<ExcelObject> excelObjects, String companyName) {
        for (ExcelObject excelObject : excelObjects) {
            if ( !excelObject.containsProp("company") )
                excelObject.addProp("company", companyName);
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
        tableHeader.putAll(getStorages());

        return tableHeader;
    }

    private Map<String, String> getStorages() {
        return storages;
    }

    private void unifyExcelObjects(Set<String> maxQuantityKeys, Set<String> maxPriceKeys) {
        LOGGER.info("unification excel objects");

        for (ExcelObject excelObject : excelObjects) {
            excelObject.replaceQuantityProps(maxQuantityKeys);
            excelObject.replacePriceProps(maxPriceKeys);
        }
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

    private List<ExcelObject> parseExcelFile(File companyFile, SearchConditions conditions) {
        LOGGER.info("parsing excel file {}", companyFile);

        if (companyFile == null || !companyFile.exists() || !companyFile.isFile()) {
            LOGGER.error("File does not exist or is not a file: {}",
                    companyFile != null ? companyFile.getAbsolutePath() : "null");
            return new ArrayList<>();
        }

        Workbook workbook = loadWorkbook(companyFile);
        if (workbook == null) {
            LOGGER.error("Failed to load workbook from file: {}", companyFile.getAbsolutePath());
            return new ArrayList<>();
        }

        if (workbook.getNumberOfSheets() == 0) {
            LOGGER.error("Workbook contains no sheets");
            closeWorkbook(workbook);
            return new ArrayList<>();
        }

        Sheet sheet = workbook.getSheetAt(0);

        if (sheet == null || sheet.getLastRowNum() <= 0) {
            LOGGER.warn("Sheet is empty or contains no data");
            closeWorkbook(workbook);
            return new ArrayList<>();
        }

        try {
            HeaderParser headerParser = new HeaderParser(sheet, 12);

            ExcelHeader excelHeader = headerParser.parseHeader();

            BodyParser bodyParser = new BodyParser(sheet, excelHeader);

            List<ExcelObject> parsedExcelObjects = bodyParser.parse(conditions);
            if ( !parsedExcelObjects.isEmpty() )
                replaceStorages(excelHeader.getStorages());

            return parsedExcelObjects;
        }
        catch (Exception exc) {
            LOGGER.error("Error parsing Excel file: {}", companyFile.getAbsolutePath(), exc);
            return new ArrayList<>();
        }
        finally {
            closeWorkbook(workbook);
        }
    }

    private void replaceStorages(Map<String, String> newStorages) {
        if (storages == null || storages.isEmpty() || storages.size() < newStorages.size()) {
            Map<String, String> countColumns = new HashMap<>();
            for (String storageKey : newStorages.keySet()) {
                countColumns.put(storageKey.replace("storage", "count"), newStorages.get(storageKey));
            }
            storages = countColumns;
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
