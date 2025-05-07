module testpackage.gestiondinventaireyrm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens testpackage.gestiondinventaireyrm to javafx.fxml;
    exports testpackage.gestiondinventaireyrm;
    requires javafx.graphics;

    // Open the stateMachin package to javafx.fxml module
    opens stateMachin to javafx.fxml;
    // Export stateMachin package to allow access from other modules
    exports stateMachin;


    // Opens your package to JavaFX

}