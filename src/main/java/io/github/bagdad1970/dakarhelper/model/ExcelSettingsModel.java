package io.github.bagdad1970.dakarhelper.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExcelSettingsModel {

    private static final Logger LOGGER = LogManager.getLogger();
    private static ExcelSettingsModel instance;
    private File rootDir;

    private ExcelSettingsModel() {
        loadData();
    }

    public static synchronized ExcelSettingsModel getInstance() {
        if (instance == null) {
            return new ExcelSettingsModel();
        }
        return instance;
    }

    private void loadData() {
        LOGGER.info("loading excel settings");
        try (FileReader reader = new FileReader("src/main/resources/settings.json")) {
            JSONTokener tokener = new JSONTokener(reader);

            JSONObject jsonObject = new JSONObject(tokener);

            rootDir = new File((String) jsonObject.get("root-dir"));
        }
        catch (IOException exception) {
            LOGGER.error("input/output failed", exception);
        }
        catch (Exception exception) {
            LOGGER.error("failed", exception);
        }
    }

    public void saveData() {
        try (FileWriter writer = new FileWriter("src/main/resources/settings.json")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("root-dir", rootDir.toString());

            writer.write(jsonObject.toString(4));
            writer.flush();
        }
        catch (IOException exception) {
            LOGGER.error("input/output failed", exception);
        }
        catch (Exception exception) {
            LOGGER.error("failed", exception);
        }
    }

    public void updateRootDir(File newRootDir) {
        rootDir = newRootDir;
    }

    public String getRootDir() {
        return rootDir.toString();
    }
}
