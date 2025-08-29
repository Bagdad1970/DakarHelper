package io.github.bagdad1970.dakarhelper.presenter;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import io.github.bagdad1970.dakarhelper.model.CompanyListModel;
import io.github.bagdad1970.dakarhelper.view.CompanyListController;
import javafx.collections.ObservableList;

public class CompanyListPresenter {

    private CompanyListController view;
    private CompanyListModel model;

    public CompanyListPresenter(CompanyListController view, CompanyListModel model) {
        this.view = view;
        this.model = model;
    }

    public void addCompany(String name) {
        model.addCompany(name);
        view.clearNameField();
        updateCompanyList();
    }

    public void removeCompanies(ObservableList<Company> companiesToRemove) {
        model.removeCompanies(companiesToRemove);
        updateCompanyList();
    }

    public ObservableList<Company> getCompanies() {
        return model.getCompanies();
    }

    public void updateCompanyList() {
        view.showCompanies();
    }

}
