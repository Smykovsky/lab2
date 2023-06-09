module pl.javastart.bookshop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires lombok;
    requires org.slf4j;
    requires com.zaxxer.hikari;
    requires java.persistence;

    opens pl.javastart.bookshop to javafx.fxml;
    opens pl.javastart.bookshop.model to lombok;
    exports pl.javastart.bookshop;
    exports pl.javastart.bookshop.controller;
    exports pl.javastart.bookshop.model;
    opens pl.javastart.bookshop.controller to javafx.fxml;
}