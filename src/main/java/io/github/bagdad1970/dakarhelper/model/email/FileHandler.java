package io.github.bagdad1970.dakarhelper.model.email;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHandler.class);

    private Map<String, Path> companyDirs;

    FileHandler(List<Company> companies) {
        this.companyDirs = new HashMap<>();

        createCompanyDirs(companies);
    }

    Map<String, Path> getCompanyDirs() {
        return companyDirs;
    }

    private void createCompanyDirs(List<Company> companies) {
        String baseDir = System.getProperty("company.dir");
        if (baseDir == null || baseDir.trim().isEmpty()) {
            throw new IllegalStateException("System property 'company.dir' is not set");
        }

        for (Company company : companies) {
            String companyName = company.getName();
            Path companyPath = Paths.get(baseDir, companyName);

            try {
                Files.createDirectories(companyPath);
                companyDirs.put(companyName, companyPath);
                LOGGER.debug("Created directory for company: {}", companyName);
            }
            catch (IOException e) {
                LOGGER.error("Failed to create directory for company: {}", companyName, e);
                throw new RuntimeException("Failed to create company directories", e);
            }
        }
    }

    void saveExcelFile(String companyName, String filename, BodyPart bodyPart) {
        Path companyDir =  companyDirs.get(companyName);
        Path filepath = companyDir.resolve(filename);

        saveFile(filepath, bodyPart);
    }

    private void saveFile(Path filepath, BodyPart bodyPart) {
        try (InputStream inputStream = bodyPart.getInputStream();
             FileOutputStream fileOutput = new FileOutputStream(filepath.toFile())) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
            }
            LOGGER.debug("Saved Excel file: {}", filepath);
        }
        catch (IOException | MessagingException e) {
            LOGGER.error("Failed to save file: {}", filepath, e);
            throw new RuntimeException("File save failed", e);
        }
    }

}
