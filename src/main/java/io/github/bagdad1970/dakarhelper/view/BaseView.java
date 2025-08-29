package io.github.bagdad1970.dakarhelper.view;

import io.github.bagdad1970.dakarhelper.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class BaseView {

    protected Stage stage;
    protected FXMLLoader loader;

    BaseView(String fxmlpath, String title, Modality modality) {
        try {
            stage = new Stage();
            loader = new FXMLLoader(App.class.getResource(fxmlpath));
            stage.setTitle(title);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(modality);
        }
        catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public Stage getStage() {
        return stage;
    }

    public void show() {
        stage.show();
    }

}
