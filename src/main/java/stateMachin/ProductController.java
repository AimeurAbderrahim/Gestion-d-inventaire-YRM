package stateMachin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ProductController extends BaseController {
    private Parent root;

    public ProductController(ControllerStateMachine stateMachine) {
        super(stateMachine);
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/testpackage/gestiondinventaireyrm/pages/ProduitPage.fxml"));
            loader.setController(this); // Attach this class to FXML controller
            root = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load ProduitPage.fxml", e);
        }
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    @Override
    public void onEnter() {
        System.out.println("Entered ProductController state.");
    }

    @FXML
    private void initialize() {
        System.out.println("ProductController FXML initialized.");
    }
}
