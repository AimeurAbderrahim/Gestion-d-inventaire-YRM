package temp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import stateMachin.BaseController;
import stateMachin.ControllerStateMachine;

public class ProductsController extends BaseController {
    @FXML private Label titleLabel;
    @FXML private ListView<String> productListView;
    @FXML private Button detailsButton;
    @FXML private Button popupButton;
    @FXML private Button locationButton;

    private boolean initialized = false;

    public ProductsController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }



    private void initialize() {
        if (initialized) return;

        // Setup button actions
      //  locationButton.setOnAction(e -> goToLocation());

        // Load items asynchronously
        Platform.runLater(() -> {
            productListView.getItems().addAll("Product 1", "Product 2", "Product 3");
        });

        initialized = true;
    }


    // Login controller doesn't have to implements Switch abstract buttons
    @Override
    @FXML
    protected void ProduitButtonSwitch(ActionEvent event) {}
    @Override
    @FXML
    protected void FournisseurButtonSwitch(ActionEvent event) {}
    @Override
    @FXML
    protected void BonsButtonSwitch(ActionEvent event) {}
    @Override
    @FXML
    protected void EmplacementButtonSwitch(ActionEvent event) {}

    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data if needed when entering this state
        if (productListView != null && productListView.getItems().isEmpty()) {
            Platform.runLater(() -> {
                productListView.getItems().addAll("Product 1", "Product 2", "Product 3");
            });
        }
    }
}//
