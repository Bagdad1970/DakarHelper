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
    requires org.apache.poi.ooxml;
    requires org.eclipse.angus.activation;
    requires org.eclipse.angus.mail;
    requires org.slf4j;
    requires ch.qos.logback.classic;

    exports io.github.bagdad1970.dakarhelper;

    exports io.github.bagdad1970.dakarhelper.datasource;

    exports io.github.bagdad1970.dakarhelper.presenter;
    opens io.github.bagdad1970.dakarhelper.presenter to javafx.fxml;

    exports io.github.bagdad1970.dakarhelper.model;
    opens io.github.bagdad1970.dakarhelper.model to javafx.fxml;

    exports io.github.bagdad1970.dakarhelper.view;
    opens io.github.bagdad1970.dakarhelper.view to javafx.fxml;
}