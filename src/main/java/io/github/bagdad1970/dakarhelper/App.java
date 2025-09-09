package io.github.bagdad1970.dakarhelper;

import io.github.bagdad1970.dakarhelper.presenter.MainPresenter;
import io.github.bagdad1970.dakarhelper.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("DakarHelper");
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(800);

        MainController mainController = fxmlLoader.getController();
        MainPresenter mainPresenter = new MainPresenter(mainController);
        mainController.setPresenter(mainPresenter);

        stage.setOnCloseRequest(event -> {
            mainPresenter.saveCompanyList();
            mainPresenter.saveRootDir();
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
