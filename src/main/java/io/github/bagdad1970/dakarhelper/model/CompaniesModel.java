package io.github.bagdad1970.dakarhelper.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CompaniesModel {

    private static final Logger LOGGER = LogManager.getLogger();

    private ObservableList<Company> companies;
    private static CompaniesModel instance;

    private CompaniesModel() {
        this.companies = FXCollections.observableArrayList();
        loadData();
    }

    public static synchronized CompaniesModel getInstance() {
        if (instance == null) {
            return new CompaniesModel();
        }
        return instance;
    }

    private void loadData() {
        LOGGER.info("loading companies");
        try (FileReader reader = new FileReader("/home/bagdad/IdeaProjects/DakarHelper/src/main/resources/company-list.json")) {
            JSONTokener tokener = new JSONTokener(reader);

            JSONArray companyArray = new JSONArray(tokener);

            for (int i=0; i<companyArray.length(); i++) {
                JSONObject company = companyArray.getJSONObject(i);
                companies.add(new Company(company.getString("name")));
            }
        }
        catch (IOException exception) {
            LOGGER.error("input/output failed", exception);
        }
        catch (Exception exception) {
            LOGGER.error("failed", exception);
        }
    }

    public void addCompany(String name) {
        LOGGER.info("adding company: {}", name);
        Company newCompany = new Company(name);
        companies.add(newCompany);
    }

    public void removeCompanies(ObservableList<Company> companiesToRemove) {
        LOGGER.info("removing companies: {}", companiesToRemove);
        companies.removeAll(companiesToRemove);
    }

    public void saveData() {
        try (FileWriter writer = new FileWriter("/home/bagdad/IdeaProjects/DakarHelper/src/main/resources/company-list.json")) {
            JSONArray companyArray = new JSONArray();
            for (Company company : companies) {
                companyArray.put(company.toJson());
            }

            writer.write(companyArray.toString(4));
            writer.flush();
        }
        catch (IOException exception) {
            LOGGER.error("input/output failed", exception);
        }
        catch (Exception exception) {
            LOGGER.error("failed", exception);
        }
    }

    public ObservableList<Company> getCompanies() {
        return companies;
    }
}
