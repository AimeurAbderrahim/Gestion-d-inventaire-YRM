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
    exports testpackage.model to javafx.fxml;
    exports testpackage.model.core to javafx.fxml;
    exports testpackage.model.enumeration to javafx.fxml;
    exports testpackage.model.errors to javafx.fxml;
    exports testpackage.model.generator to javafx.fxml;
    opens db.configuration to javafx.fxml;
    opens db.errors to javafx.fxml;
    opens db.test to javafx.fxml;

    exports testpackage.gestiondinventaireyrm;
    requires javafx.graphics;

    exports stateMachin to javafx.graphics;
}
