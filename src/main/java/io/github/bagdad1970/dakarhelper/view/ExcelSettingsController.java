package io.github.bagdad1970.dakarhelper.view;

import io.github.bagdad1970.dakarhelper.presenter.ExcelSettingsPresenter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;


public class ExcelSettingsController {

    private ExcelSettingsPresenter presenter;

    @FXML
    private TextField filePathField;

    @FXML
    private Button dirChooserButton;

    public void setPresenter(ExcelSettingsPresenter presenter) {
        this.presenter = presenter;
    }

    @FXML
    public void openFileChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Выберите файл");

        Stage stage = (Stage) dirChooserButton.getScene().getWindow();
        File selectedFile = dirChooser.showDialog(stage);

        if (selectedFile != null) {
            presenter.updateRootDir(selectedFile);
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    public void updateRootDir() {
        filePathField.setText(presenter.getRootDir());
    }

}
