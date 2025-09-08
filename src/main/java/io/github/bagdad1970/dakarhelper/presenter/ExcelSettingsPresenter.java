package io.github.bagdad1970.dakarhelper.presenter;

import io.github.bagdad1970.dakarhelper.datasource.Company;
import io.github.bagdad1970.dakarhelper.model.CompaniesModel;
import io.github.bagdad1970.dakarhelper.model.ExcelSettingsModel;
import io.github.bagdad1970.dakarhelper.view.CompaniesController;
import io.github.bagdad1970.dakarhelper.view.ExcelSettingsController;
import io.github.bagdad1970.dakarhelper.view.ExcelSettingsView;
import javafx.collections.ObservableList;

import java.io.File;
import java.nio.file.Path;

public class ExcelSettingsPresenter {

    private ExcelSettingsController view;
    private ExcelSettingsModel model;

    public ExcelSettingsPresenter(ExcelSettingsController view, ExcelSettingsModel model) {
        this.view = view;
        this.model = model;
    }

    public void updateRootDir(File newRootDir) {
        model.updateRootDir(newRootDir);
    }

    public String getRootDir() {
        return model.getRootDir();
    }

}
