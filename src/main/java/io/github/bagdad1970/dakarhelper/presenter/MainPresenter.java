package io.github.bagdad1970.dakarhelper.presenter;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import io.github.bagdad1970.dakarhelper.datasource.SearchConditions;
import io.github.bagdad1970.dakarhelper.model.CompaniesModel;
import io.github.bagdad1970.dakarhelper.model.email.EmailHandler;
import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelObject;
import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelParser;
import io.github.bagdad1970.dakarhelper.view.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;


public class MainPresenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainPresenter.class);

    private final MainController view;
    private final CompaniesModel companiesModel = CompaniesModel.getInstance();

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

    public void processData() {
        SearchConditions conditions = new SearchConditions(view.getName(), view.getQuantity());

        Task<Void> startTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    List<Company> companies = companiesModel.getCompanies();

                    if (companies.isEmpty()) {
                        Platform.runLater(() -> {
                            view.showErrorAlert("Ошибка", "Список компаний пуст. Добавьте компании в список.");
                        });
                        return null;
                    }

                    EmailHandler emailHandler = new EmailHandler(companies);
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
                    LOGGER.error("Error:", e);
                    Platform.runLater(() -> {
                        view.showErrorAlert("Ошибка", "Непредвиденная ошибка: " + e.getMessage());
                    });
                    return null;
                }
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                LOGGER.info("Task completed successfully");
                Platform.runLater(() -> {
                    view.enableButton();
                });
            }

            @Override
            protected void failed() {
                super.failed();
                LOGGER.error("Task failed", getException());
                Platform.runLater(() -> {
                    view.enableButton();
                });
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

}
