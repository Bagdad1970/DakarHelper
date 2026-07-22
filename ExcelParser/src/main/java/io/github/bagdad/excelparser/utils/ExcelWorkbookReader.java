package io.github.bagdad.excelparser.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class ExcelWorkbookReader implements AutoCloseable {

    @Getter
    private Workbook workbook;

    public ExcelWorkbookReader(Path filepath) {
        workbook = loadWorkbook(filepath);
    }

    public static Workbook loadWorkbook(Path filepath) {
        if (!Files.exists(filepath)) {
            log.error("{} does not exist", filepath);
            return null;
        }

        if (!Files.isRegularFile(filepath)) {
            log.error("{} is not a file", filepath);
            return null;
        }

        try (FileInputStream excelFileStream = new FileInputStream(filepath.toFile())) {
            String filename = filepath.getFileName().toString();

            if (filename.endsWith(".xls")) {
                return new HSSFWorkbook(excelFileStream);
            }
            else if (filename.endsWith(".xlsx")) {
                return new XSSFWorkbook(excelFileStream);
            }
            else {
                log.error("Unsupported file format in {}", filepath.getFileName());
                return null;
            }
        }
        catch (IOException exc) {
            log.error("I/O exception while loading workbook", exc);
            return null;
        }
    }

    public Sheet getFirstSheet() {
        if (workbook == null) {
            return null;
        }

        if (workbook.getNumberOfSheets() == 0) {
            log.error("Workbook contains no sheets");
            this.close();
            return null;
        }

        return workbook.getSheetAt(0);
    }

    @Override
    public void close() {
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
