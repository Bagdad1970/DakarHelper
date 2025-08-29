module io.github.bagdad1970.dakarhelper {
    requires javafx.controls;
    requires javafx.fxml;


    opens io.github.bagdad1970.dakarhelper to javafx.fxml;
    exports io.github.bagdad1970.dakarhelper;
}