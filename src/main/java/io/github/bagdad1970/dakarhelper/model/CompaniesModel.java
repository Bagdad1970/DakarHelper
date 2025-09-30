package io.github.bagdad1970.dakarhelper.model;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class CompaniesModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompaniesModel.class);
    private static CompaniesModel instance;

    private ObservableList<Company> companies;
    private File dataFile;

    private CompaniesModel() {
        this.companies = FXCollections.observableArrayList();
        initDataFile();
        loadData();
    }

    public static synchronized CompaniesModel getInstance() {
        if (instance == null) {
            instance = new CompaniesModel();
        }
        return instance;
    }

    private void initDataFile() {
        String userHome = System.getProperty("user.home");
        dataFile = new File(userHome, ".dakarhelper/company-list.json");
        dataFile.getParentFile().mkdirs();
    }

    private void loadData() {
        LOGGER.info("Loading companies from: {}", dataFile.getAbsolutePath());

        if (!dataFile.exists()) {
            createDefaultData();
            return;
        }

        try (FileReader reader = new FileReader(dataFile)) {
            JSONArray companyArray = new JSONArray(new JSONTokener(reader));

            for (int i = 0; i < companyArray.length(); i++) {
                JSONObject company = companyArray.getJSONObject(i);
                companies.add(new Company(company.getString("name")));
            }

            LOGGER.info("Loaded companies: {}", companies);
        }
        catch (Exception e) {
            LOGGER.error("Failed to load companies data", e);
            createDefaultData();
        }
    }

    private void createDefaultData() {
        companies.clear();
        saveData();
        LOGGER.info("Created default companies data");
    }

    public void addCompany(String name) {
        LOGGER.info("Adding company: {}", name);
        companies.add(new Company(name));
        saveData();
    }

    public void removeCompanies(ObservableList<Company> companiesToRemove) {
        LOGGER.info("Removing companies: {}", companiesToRemove);
        companies.removeAll(companiesToRemove);
        saveData();
    }

    public void saveData() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            JSONArray companyArray = new JSONArray();
            for (Company company : companies) {
                companyArray.put(company.toJson());
            }

            writer.write(companyArray.toString(4));
            LOGGER.info("Saved companies to file: {}", companies);
        }
        catch (Exception e) {
            LOGGER.error("Failed to save companies data", e);
        }
    }

    public ObservableList<Company> getCompanies() {
        return companies;
    }
}