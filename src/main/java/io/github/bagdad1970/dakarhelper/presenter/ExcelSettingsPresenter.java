package io.github.bagdad1970.dakarhelper.presenter;

import io.github.bagdad1970.dakarhelper.model.ExcelSettingsModel;
import io.github.bagdad1970.dakarhelper.view.ExcelSettingsController;

import java.io.File;

public class ExcelSettingsPresenter {

    private ExcelSettingsController view;
    private ExcelSettingsModel model;

    public ExcelSettingsPresenter(ExcelSettingsController view, ExcelSettingsModel model) {
        this.view = view;
        this.model = model;
    }

    public void setRootDir(File newRootDir) {
        model.setRootDir(newRootDir);
    }

    public String getRootDir() {
        return model.getRootDir();
    }

}
