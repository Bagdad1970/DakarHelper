package io.github.bagdad.emailhandler;

import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FileHandler {

    private final Map<String, Path> vendorDirs;

    private final String saveDir;

    FileHandler(String saveDir) {
        this.vendorDirs = new HashMap<>();
        this.saveDir = saveDir;
    }

    public void createVendorDir(String vendorTitle) {
        Path vendorDirPath = Paths.get(saveDir, vendorTitle);
        try {
            Files.createDirectories(vendorDirPath);
            vendorDirs.put(vendorTitle, vendorDirPath);
            log.info("Directory created for vendor '{}': {}", vendorTitle, vendorDirPath);
        }
        catch (IOException e) {
            log.error("Failed to create directory for vendor '{}': {}", vendorTitle, vendorDirPath, e);
            throw new RuntimeException("Failed to create vendor directories", e);
        }
        catch (Exception e) {
            log.error("Unexpected error creating directory for '{}'", vendorTitle, e);
        }
    }

    public Path saveExcelFile(String vendorTitle, String filename, BodyPart bodyPart) {
        if (!vendorDirs.containsKey(vendorTitle)) {
            createVendorDir(vendorTitle);
        }

        Path vendorDir = vendorDirs.get(vendorTitle);
        Path filepath = vendorDir.resolve(filename);
        log.info("Saving Excel file: {}", filepath);
        saveFile(filepath, bodyPart);
        return filepath;
    }

    private static void saveFile(Path filepath, BodyPart bodyPart) {
        try (InputStream inputStream = bodyPart.getInputStream();
             FileOutputStream fileOutput = new FileOutputStream(filepath.toFile())) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
            }
            log.info("Saved file: {}", filepath);
        }
        catch (IOException | MessagingException e) {
            log.error("Saving file failed for {}", filepath, e);
            throw new RuntimeException("Saving file failed", e);
        }
        catch (Exception e) {
            log.error("Unexpected error saving file: {}", filepath, e);
        }
    }
}