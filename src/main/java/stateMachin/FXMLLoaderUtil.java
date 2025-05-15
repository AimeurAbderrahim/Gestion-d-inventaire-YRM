package stateMachin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.net.URL;

public class FXMLLoaderUtil {

    /**
     * Loads an FXML file and returns the root element
     * @param fxmlPath The path to the FXML file (e.g., "/fxml/ProductView.fxml")
     * @param controller The controller instance to use
     * @return The root element of the FXML file
     */
    public static Parent loadFXML(String fxmlPath, Object controller) {
        try {
            URL fxmlUrl = controller.getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }//

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            loader.setController(controller);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML: " + e.getMessage(), e);
        }
    }

    /**
     * Loads an FXML file that contains its own controller definition
     * @param fxmlPath The path to the FXML file
     * @return An object containing both the root element and the controller
     */
    public static FXMLLoadResult loadFXMLWithController(String fxmlPath) {
        try {
            URL fxmlUrl = FXMLLoaderUtil.class.getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Object controller = loader.getController();
            return new FXMLLoadResult(root, controller);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML: " + e.getMessage(), e);
        }
    }

    /**
     * Class to hold the result of loading an FXML file with its controller
     */
    public static class FXMLLoadResult {
        private final Parent root;
        private final Object controller;

        public FXMLLoadResult(Parent root, Object controller) {
            this.root = root;
            this.controller = controller;
        }

        public Parent getRoot() {
            return root;
        }

        public <T> T getController() {
            return (T) controller;
        }
    }
}