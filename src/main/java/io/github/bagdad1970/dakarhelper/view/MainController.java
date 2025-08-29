package io.github.bagdad1970.dakarhelper.view;

import io.github.bagdad1970.dakarhelper.presenter.MainPresenter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;


public class MainController {

    @FXML
    private Button findButton;

    @FXML
    private Spinner itemCount;

    @FXML
    private TextField itemName;

    @FXML
    private MenuItem companyList;

    private MainPresenter presenter;

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    @FXML
    private void openCompanyList() {
        presenter.openCompanyList();
    }

    @FXML
    private void processData() {
        presenter.processData();
    }

}
