package io.github.bagdad.emailhandler;

import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileHandler {

    private final Path saveDir;

    public FileHandler(Path saveDir) {
        this.saveDir = saveDir;
    }

    public Path saveExcelFile(String vendorTitle, String filename, BodyPart bodyPart) {
        Path createdVendorDirectory = createDirectory(vendorTitle);

        Path filepath = createdVendorDirectory.resolve(filename);
        saveFile(filepath, bodyPart);
        return filepath;
    }

    public Path createDirectory(String vendorTitle) {
        Path vendorDiriectoryPath = saveDir.resolve(vendorTitle);

        try {
            Path createdVendorDirPath = Files.createDirectories(saveDir.resolve(vendorTitle));
            log.info("Directory created for vendor '{}': {}", vendorTitle, createdVendorDirPath);
            return createdVendorDirPath;
        }
        catch (FileAlreadyExistsException e) {
            log.error("Directory already exists for vendor '{}': {}", vendorTitle, vendorDiriectoryPath);
            throw new RuntimeException("Failed to create vendor directory", e);
        }
        catch (IOException e) {
            log.error("Failed to create directory for vendor '{}': {}", vendorTitle, vendorDiriectoryPath);
            throw new RuntimeException("Failed to create vendor directory", e);
        }
        catch (Exception e) {
            log.error("Unhandled exception when creating vendor directory '{}': {}", vendorTitle, vendorDiriectoryPath);
            throw new RuntimeException("Failed to create vendor directory", e);
        }
    }

    private static void saveFile(Path filepath, BodyPart bodyPart) {
        log.info("Saving Excel file: {}", filepath);
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