module SirMangaNoxV {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.jsoup;
    requires org.xerial.sqlitejdbc;

    opens views.home to javafx.fxml;
    opens views.addmanga to javafx.fxml;
    opens views.popup to javafx.fxml;
    exports main;
}