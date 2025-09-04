package io.github.bagdad1970.dakarhelper.view;

import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelObject;
import io.github.bagdad1970.dakarhelper.presenter.MainPresenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    private TableView<ExcelObject> tableView;

    @FXML
    private Button findButton;

    @FXML
    private Spinner<Integer> itemCount;

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

    public String getName() {
        return itemName.getText();
    }

    public int getQuantity() {
        return itemCount.getValue();
    }

    public void setTableView(List<ExcelObject> excelObjects) {
        setTableViewHeader();
    }

    private void setTableViewHeader() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemCount.setEditable(true);
    }
}
