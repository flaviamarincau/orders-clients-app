module java{
       requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens presentation to javafx.fxml;
    exports presentation;
    opens connection to java.sql;
    opens businessLayer to javafx.fxml;
    exports model;
    exports start to javafx.graphics;
    exports connection;
    exports businessLayer;
    exports dataAcessLayer;
}