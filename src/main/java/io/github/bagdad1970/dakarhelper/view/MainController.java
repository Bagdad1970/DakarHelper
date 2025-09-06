package io.github.bagdad1970.dakarhelper.view;

import io.github.bagdad1970.dakarhelper.model.parser.excel.ExcelObject;
import io.github.bagdad1970.dakarhelper.presenter.MainPresenter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Map;
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
    private Button companies;

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

    private void clearTable() {
        tableView.getItems().clear();
        tableView.getColumns().clear();
    }

    public void updateTableView(Map<String, String> tableHeader, ObservableList<ExcelObject> excelObjects) {
        clearTable();
        updateTableViewHeader(tableHeader);
        tableView.setItems(excelObjects);
    }

    private void updateTableViewHeader(Map<String, String> tableHeader) {
        if (tableHeader != null && !tableHeader.isEmpty()) {
            for (String key : tableHeader.keySet()) {
                String tableName = tableHeader.get(key);
                TableColumn<ExcelObject, ?> tableColumn = new TableColumn<>(tableName);

                switch (key) {
                    case "price", "retail", "wholesale":
                        TableColumn<ExcelObject, Double> doubleColumn = (TableColumn<ExcelObject, Double>) tableColumn;
                        doubleColumn.setCellValueFactory(cellData -> {
                            Object value = cellData.getValue().getProp(key);
                            return new SimpleDoubleProperty(value != null ? Double.parseDouble(value.toString()) : 0).asObject();
                        });
                        break;
                    case "num", "count1", "count2": // очень плохо
                        TableColumn<ExcelObject, Integer> integerColumn = (TableColumn<ExcelObject, Integer>) tableColumn;
                        integerColumn.setCellValueFactory(cellData -> {
                            Object value = cellData.getValue().getProp(key);
                            return new SimpleIntegerProperty(value != null ? Integer.parseInt(value.toString()) : 0).asObject();
                        });
                        break;
                    case "name":
                    default:
                        TableColumn<ExcelObject, String> stringColumn = (TableColumn<ExcelObject, String>) tableColumn;
                        stringColumn.setCellValueFactory(cellData -> {
                            Object value = cellData.getValue().getProp(key);
                            return new SimpleStringProperty(value != null ? value.toString() : "");
                        });
                        break;
                }
                tableView.getColumns().add(tableColumn);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemCount.setEditable(true);

        Text tableViewPlaceHolder = new Text("Нет данных для отображения");
        tableViewPlaceHolder.setFont(Font.font(15));
        tableView.setPlaceholder(tableViewPlaceHolder);
    }
}
