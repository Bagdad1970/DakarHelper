package io.github.bagdad1970.dakarhelper.presenter;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import io.github.bagdad1970.dakarhelper.datasource.SearchConditions;
import io.github.bagdad1970.dakarhelper.model.CompaniesModel;
import io.github.bagdad1970.dakarhelper.model.parser.EmailHandler;
import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelObject;
import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelParser;
import io.github.bagdad1970.dakarhelper.view.CompaniesController;
import io.github.bagdad1970.dakarhelper.view.CompaniesView;
import io.github.bagdad1970.dakarhelper.view.MainController;
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

    private static final Logger LOGGER = LogManager.getLogger();

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

        SearchConditions conditions = new SearchConditions(view.getName(), 0);

        Task<Void> startTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                LOGGER.info("reading email in task");

                List<Company> companies = companiesModel.getCompanies();

                EmailHandler emailHandler = new EmailHandler("/home/bagdad/Downloads/dakarhhelper-folders", companies);
                emailHandler.readEmail();

                Map<String, Path> companyDirs = emailHandler.getCompanyDirs();

                ExcelParser excelParser = new ExcelParser(companyDirs);

                excelParser.parseExcelFiles(conditions);

                Map<String, String> tableHeader = excelParser.getTableHeader();

                Platform.runLater(() -> {
                    updateTableView(tableHeader, excelParser.getExcelObjects());
                });

                return null;
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
