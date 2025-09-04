package io.github.bagdad1970.dakarhelper.view;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import io.github.bagdad1970.dakarhelper.presenter.CompaniesPresenter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CompaniesController implements Initializable {

    private CompaniesPresenter presenter;

    @FXML
    public Button removeButton;

    @FXML
    private Button openCompaniesButton;

    @FXML
    private ListView<Company> companyList;

    @FXML
    private Button addButton;

    @FXML
    private TextField companyName;

    public void setPresenter(CompaniesPresenter presenter) {
        this.presenter = presenter;
    }

    public void showCompanies() {
        ObservableList<Company> companies = presenter.getCompanies();
        companyList.setItems(companies);
    }

    public String getCompanyName() {
        return companyName.getText();
    }

    public void addCompany() {
        String newCompanyName = getCompanyName();
        if ( !newCompanyName.isEmpty() )
            presenter.addCompany(newCompanyName);
    }

    public void clearNameField() {
        companyName.clear();
    }

    public void removeCompanies() {
        ObservableList<Company> companiesToRemove = companyList.getSelectionModel().getSelectedItems();
        System.out.println(companiesToRemove);
        presenter.removeCompanies(companiesToRemove);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        companyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

}
