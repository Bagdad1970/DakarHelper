package io.github.bagdad1970.dakarhelper.presenter;

import io.github.bagdad1970.dakarhelper.App;
import io.github.bagdad1970.dakarhelper.datasource.Company;
import io.github.bagdad1970.dakarhelper.model.CompanyListModel;
import io.github.bagdad1970.dakarhelper.model.parser.EmailHandler;
import io.github.bagdad1970.dakarhelper.view.CompanyListController;
import io.github.bagdad1970.dakarhelper.view.CompanyListView;
import io.github.bagdad1970.dakarhelper.view.MainController;
import jakarta.mail.MessagingException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class MainPresenter {

    private MainController view;
    private final CompanyListModel model = CompanyListModel.getInstance();

    public MainPresenter(MainController view) {
        this.view = view;
    }

    public void openCompanyList() {
        CompanyListView view = new CompanyListView("companyList-view.fxml", "Список компаний", Modality.WINDOW_MODAL);
        view.getStage().setResizable(false);

        FXMLLoader loader = view.getLoader();
        CompanyListController companiesController = loader.getController();
        CompanyListPresenter companiesPresenter = new CompanyListPresenter(companiesController, model);
        companiesController.setPresenter(companiesPresenter);

        view.show();
        companiesController.showCompanies();
    }

    public void processData() {
        List<Company> companyList = model.getCompanies();
        EmailHandler emailHandler = new EmailHandler("/home/bagdad/Downloads/dakarhhelper-folders", companyList);
        emailHandler.readEmail();
    }

    public void saveCompanyList() {
        model.saveData();
    }

}
