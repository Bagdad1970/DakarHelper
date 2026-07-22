package io.github.bagdad.findhandler;

import io.github.bagdad.common.ConfigManager;
import io.github.bagdad.findhandler.config.FileHandlerConfig;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@NoArgsConstructor
public class FileHandler {

    static {
        ConfigManager.loadConfig("FileHandler/src/main/resources/config/file-handler.json", FileHandlerConfig.class);
    }

    public static Path saveVendorFile(String vendorTitle, String filename, InputStream inputStream) {
        Path createdVendorDirectory = createDirectory(vendorTitle);

        Path filepath = createdVendorDirectory.resolve(filename);
        saveFile(filepath, inputStream);
        return filepath;
    }

    public static Path createDirectory(String vendorTitle) {
        log.info("Creating directory for vendor {}", vendorTitle);

        Path vendorDirectoryPath = ConfigManager.getConfig(FileHandlerConfig.class).getSaveDir()
                .resolve(vendorTitle);

        try {
            return Files.createDirectories(vendorDirectoryPath);
        }
        catch (FileAlreadyExistsException e) {
            log.error("Directory already exists for vendor '{}': {}", vendorTitle, vendorDirectoryPath);
            throw new RuntimeException("Failed to create vendor directory", e);
        }
        catch (IOException e) {
            log.error("Failed to create directory for vendor '{}': {}", vendorTitle, vendorDirectoryPath);
            throw new RuntimeException("Failed to create vendor directory", e);
        }
    }

    private static void saveFile(Path filepath, InputStream inputStream) {
        log.info("Saving file: {}", filepath);

        try (inputStream;
             FileOutputStream fileOutput = new FileOutputStream(filepath.toFile())) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutput.write(buffer, 0, bytesRead);
            }
        }
        catch (IOException e) {
            log.error("Saving file failed for {}", filepath, e);
            throw new RuntimeException("Saving file failed", e);
        }
    }

    public static void deleteFile(Path filepath) {
        log.info("Deleting file: {}", filepath);

        try {
            Files.deleteIfExists(filepath);
        }
        catch (IOException e) {
            log.error("Failed to delete the file: {}", filepath);
        }
    }

}