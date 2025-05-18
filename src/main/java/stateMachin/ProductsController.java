package stateMachin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class ProductsController extends BaseController {
    @FXML private AnchorPane productsPane;
    @FXML private TableView<?> productsTable;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private boolean initialized = false;

    // Default constructor for FXML loader
    public ProductsController() {
        super();
    }

    // Constructor for manual instantiation
    public ProductsController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }


    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data when entering this screen
        refreshProductData();
    }

    private void refreshProductData() {
        System.out.println("Refreshing product data");
        // Load/reload product data into the table
    }
}