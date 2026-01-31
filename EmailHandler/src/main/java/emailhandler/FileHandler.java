package emailhandler;

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
import java.util.List;
import java.util.Map;

@Slf4j
public class FileHandler {

    private final Map<String, Path> vendorDirs;
    private final String saveDir;

    FileHandler(List<String> vendors, String saveDir) {
        this.vendorDirs = new HashMap<>();
        this.saveDir = saveDir;

        createVendorDirs(vendors);
    }

    private void createVendorDirs(List<String> vendors) {
        for (String vendor : vendors) {
            Path vendorDirPath = Paths.get(saveDir, vendor);
            try {
                Files.createDirectories(vendorDirPath);
                vendorDirs.put(vendor, vendorDirPath);
                log.info("Directory created for vendor '{}': {}", vendor, vendorDirPath);
            }
            catch (IOException e) {
                log.error("Failed to create directory for vendor '{}': {}", vendor, vendorDirPath, e);
                throw new RuntimeException("Failed to create vendor directories", e);
            }
            catch (Exception e) {
                log.error("Unexpected error creating directory for '{}'", vendor, e);
            }
        }
    }

    public Path saveExcelFile(String vendorTitle, String filename, BodyPart bodyPart) {
        Path vendorDir = vendorDirs.get(vendorTitle);
        if (vendorDir == null) {
            log.error("No directory registered for company '{}'", vendorTitle);
            return null;
        }

        Path filepath = vendorDir.resolve(filename);
        log.info("Saving Excel file: {}/{}", vendorTitle, filename);
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