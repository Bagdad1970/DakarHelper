module io.github.bagdad1970.dakarhelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires org.apache.poi.poi;
    requires org.json;
    requires jxls.jexcel;
    requires jakarta.mail;
    requires org.apache.xmlbeans;
    requires org.apache.logging.log4j;
    requires log4j;
    requires org.apache.poi.ooxml;
    requires commons.collections;
    requires java.desktop;


    exports io.github.bagdad1970.dakarhelper.datasource;

    exports io.github.bagdad1970.dakarhelper.presenter;
    opens io.github.bagdad1970.dakarhelper.presenter to javafx.fxml;

    exports io.github.bagdad1970.dakarhelper.model;
    opens io.github.bagdad1970.dakarhelper.model to javafx.fxml;

    exports io.github.bagdad1970.dakarhelper.view;
    opens io.github.bagdad1970.dakarhelper.view to javafx.fxml;

    opens io.github.bagdad1970.dakarhelper to javafx.fxml;
    exports io.github.bagdad1970.dakarhelper;
}