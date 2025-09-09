package io.github.bagdad1970.dakarhelper.presenter;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import io.github.bagdad1970.dakarhelper.datasource.SearchConditions;
import io.github.bagdad1970.dakarhelper.model.CompaniesModel;
import io.github.bagdad1970.dakarhelper.model.ExcelSettingsModel;
import io.github.bagdad1970.dakarhelper.model.parser.excel.EmailHandler;
import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelObject;
import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelParser;
import io.github.bagdad1970.dakarhelper.view.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;


public class MainPresenter {

    private static final Logger LOGGER = LogManager.getLogger(MainPresenter.class);

    private final MainController view;
    private final CompaniesModel companiesModel = CompaniesModel.getInstance();
    private final ExcelSettingsModel excelSettingsModel = ExcelSettingsModel.getInstance();

    public MainPresenter(MainController view) {
        this.view = view;
    }

    public void openCompanyList() {
        CompaniesView view = new CompaniesView("companyList-view.fxml", "Список компаний", Modality.WINDOW_MODAL);
        view.getStage().setResizable(false);

        FXMLLoader loader = view.getLoader();
        CompaniesController companiesController = loader.getController();
        CompaniesPresenter companiesPresenter = new CompaniesPresenter(companiesController, companiesModel);
        companiesController.setPresenter(companiesPresenter);

        view.show();
        companiesController.showCompanies();
    }

    public void openExcelSettings() {
        ExcelSettingsView view = new ExcelSettingsView("excel-settings.fxml", "Настройки обработки Excel", Modality.WINDOW_MODAL);
        view.getStage().setResizable(false);

        FXMLLoader loader = view.getLoader();
        ExcelSettingsController excelSettingsController = loader.getController();
        ExcelSettingsPresenter excelSettingsPresenter = new ExcelSettingsPresenter(excelSettingsController, excelSettingsModel);
        excelSettingsController.setPresenter(excelSettingsPresenter);

        excelSettingsController.setRootDirField();
        view.show();
    }

    public void processData() {
        SearchConditions conditions = new SearchConditions(view.getName(), view.getQuantity());

        Task<Void> startTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    LOGGER.info("reading email in task");

                    List<Company> companies = companiesModel.getCompanies();

                    if (companies.isEmpty()) {
                        Platform.runLater(() -> {
                            view.showErrorAlert("Ошибка", "Список компаний пуст. Добавьте компании в список.");
                        });
                        return null;
                    }

                    EmailHandler emailHandler = new EmailHandler(excelSettingsModel.getRootDir(), companies);
                    emailHandler.readEmail();

                    Map<String, Path> companyDirs = emailHandler.getCompanyDirs();

                    ExcelParser excelParser = new ExcelParser(companyDirs);
                    excelParser.parseExcelFiles(conditions);

                    Map<String, String> tableHeader = excelParser.getTableHeader();
                    ObservableList<ExcelObject> excelObjects = excelParser.getExcelObjects();

                    if (excelObjects.isEmpty()) {
                        Platform.runLater(() -> {
                            view.showInfoAlert("Результат поиска", "Товары не найдены по заданным критериям.");
                        });
                    }

                    Platform.runLater(() -> {
                        updateTableView(tableHeader, excelObjects);
                    });

                    return null;

                }
                catch (Exception e) {
                    LOGGER.error("Ошибка при обработке данных", e);
                    Platform.runLater(() -> {
                        view.showErrorAlert("Ошибка", "Произошла ошибка при обработке данных: " + e.getMessage());
                    });
                    return null;
                }
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                LOGGER.info("Task completed successfully");
            }

            @Override
            protected void failed() {
                super.failed();
                LOGGER.error("Task failed", getException());
            }
        };

        new Thread(startTask).start();
    }

    public void updateTableView(Map<String, String> tableHeader, ObservableList<ExcelObject> excelObjects) {
        view.updateTableView(tableHeader, excelObjects);
    }

    public void saveCompanyList() {
        companiesModel.saveData();
    }

    public void saveRootDir() {
        excelSettingsModel.saveData();
    }

}
