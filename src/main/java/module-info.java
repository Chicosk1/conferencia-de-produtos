module com.example.conferencia_de_produtos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    exports  dva;
    opens dva to javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.example.conferencia_de_produtos to javafx.fxml;
    exports com.example.conferencia_de_produtos;
}