package io.github.bagdad.excelparser.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelWorkbookHandler {

    public static Workbook loadWorkbook(String vendorFilepath) {
        File vendorFile = new File(vendorFilepath);

        if (!vendorFile.exists() || !vendorFile.isFile()) {
            log.error("File does not exist or is not a file: \"{}\"", vendorFile.getAbsolutePath());
            return null;
        }

        Workbook workbook = null;
        try (FileInputStream excelFileStream = new FileInputStream(vendorFile)) {
            if (vendorFile.getName().endsWith(".xls")) {
                workbook = new HSSFWorkbook(excelFileStream);
            }
            else if (vendorFile.getName().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(excelFileStream);
            }
            else {
                log.warn("Unsupported file format: {}", vendorFile.getName());
                return null;
            }
        }
        catch (FileNotFoundException exc) {
            log.error("File not found", exc);
        }
        catch (Exception exc) {
            log.error("Error", exc);
        }
        return workbook;
    }

    public static List<Sheet> getSheets(Workbook vendorFileWorkbook) {
        if (vendorFileWorkbook == null) {
            return null;
        }

        if (vendorFileWorkbook.getNumberOfSheets() == 0) {
            log.error("Workbook contains no sheets");
            ExcelWorkbookHandler.closeWorkbook(vendorFileWorkbook);
            return null;
        }

        List<Sheet> sheets = new ArrayList<>();
        for (int i = 0; i < vendorFileWorkbook.getNumberOfSheets(); i++) {
            sheets.add(vendorFileWorkbook.getSheetAt(i));
        }

        return sheets;
    }

    public static void closeWorkbook(Workbook workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            }
            catch (IOException exc) {
                log.error("Error closing workbook", exc);
            }
        }
    }

}
