package io.github.bagdad1970.dakarhelper;

import io.github.bagdad1970.dakarhelper.presenter.MainPresenter;
import io.github.bagdad1970.dakarhelper.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class DakarHelperApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        initAppDir();

        FXMLLoader fxmlLoader = new FXMLLoader(DakarHelperApplication.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("DakarHelper");
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(800);

        MainController mainController = fxmlLoader.getController();
        MainPresenter mainPresenter = new MainPresenter(mainController);
        mainController.setPresenter(mainPresenter);

        stage.setOnCloseRequest(event -> mainController.saveCompanyList());

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                mainController.processData();
            }
        });

        stage.show();
    }

    private static void initAppDir() {
        String osName = System.getProperty("os.name");

        if (osName.contains("Linux")) {
            String appDir = System.getProperty("user.home") + "/.dakarhelper";
            new File(appDir).mkdirs();

            System.setProperty("company.dir", appDir + "/company_dirs");
            new File(System.getProperty("company.dir")).mkdirs();

            System.setProperty("log.dir", appDir + "/logs");
            new File(System.getProperty("log.dir")).mkdirs();

            System.setProperty("app.config", appDir + "/config");
            new File(System.getProperty("app.config")).mkdirs();
        }
        else if (osName.contains("Windows")) {
            String appDir = System.getenv("LOCALAPPDATA") + "\\DakarHelper";

            System.setProperty("company.dir", appDir + "\\company_dirs");
            new File(System.getProperty("company.dir")).mkdirs();

            System.setProperty("log.dir", appDir + "\\logs");
            new File(System.getProperty("log.dir")).mkdirs();

            System.setProperty("app.config", appDir + "\\config");
            new File(System.getProperty("app.config")).mkdirs();
        }
    }

}
