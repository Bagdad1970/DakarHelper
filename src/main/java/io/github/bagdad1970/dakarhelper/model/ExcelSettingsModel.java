package io.github.bagdad1970.dakarhelper.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

public class ExcelSettingsModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelSettingsModel.class);
    private static ExcelSettingsModel instance;
    private File rootDir;
    private File settingsFile;

    private ExcelSettingsModel() {
        initSettingsFile();
        loadData();
    }

    public static synchronized ExcelSettingsModel getInstance() {
        if (instance == null) {
            instance = new ExcelSettingsModel();
        }
        return instance;
    }

    private void initSettingsFile() {
        String userHome = System.getProperty("user.home");
        settingsFile = new File(userHome, ".dakarhelper/settings.json");
        settingsFile.getParentFile().mkdirs();
    }

    private void loadData() {
        LOGGER.info("Loading settings from: {}", settingsFile.getAbsolutePath());

        if (!settingsFile.exists()) {
            createDefaultSettings();
            return;
        }

        try (FileReader reader = new FileReader(settingsFile)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            rootDir = new File(jsonObject.getString("root-dir"));
        } catch (Exception e) {
            LOGGER.error("Failed to load settings", e);
            createDefaultSettings();
        }
    }

    private void createDefaultSettings() {
        rootDir = new File(System.getProperty("user.home"));
        saveData();
        LOGGER.info("Created default settings");
    }

    public void saveData() {
        try (FileWriter writer = new FileWriter(settingsFile)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("root-dir", rootDir.getAbsolutePath());
            writer.write(jsonObject.toString(4));
        } catch (Exception e) {
            LOGGER.error("Failed to save settings", e);
        }
    }

    public void setRootDir(File newRootDir) {
        rootDir = newRootDir;
        saveData();
    }

    public String getRootDir() {
        return rootDir.getAbsolutePath();
    }
}