module testpackage.gestiondinventaireyrm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    //requires java.io;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens testpackage.gestiondinventaireyrm to javafx.fxml;
    opens db.configuration to javafx.fxml;
    opens db.errors to javafx.fxml;
    opens db.test to javafx.fxml;

    exports testpackage.gestiondinventaireyrm;
    exports db.configuration;
    exports db.errors;
    exports db.test;
}
