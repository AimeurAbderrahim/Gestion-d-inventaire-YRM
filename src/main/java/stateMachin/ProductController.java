package stateMachin;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProductController extends BaseController {
    private VBox root;
    private ListView<String> listView;
    private boolean initialized = false;

    public ProductController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }

    private void lazyInit() {
        if (initialized) return;

        root = new VBox(10);
        root.setPadding(new Insets(15));

        // Header
        Label title = new Label("Product Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Content - using a more efficient cell factory approach
        listView = new ListView<>();

        // Load items asynchronously
        Platform.runLater(() -> {
            listView.getItems().addAll("Product 1", "Product 2", "Product 3");
        });

        VBox.setVgrow(listView, Priority.ALWAYS);

        // Action buttons
        HBox actions = new HBox(10);
        Button detailsBtn = new Button("Details");
        detailsBtn.setOnAction(e -> showDetails());

        Button popupBtn = new Button("Pop Up");
        popupBtn.setOnAction(e -> showPopUp());

        Button locationBtn = new Button("Go to Location");
        locationBtn.setOnAction(e -> goToLocation());

        actions.getChildren().addAll(detailsBtn, popupBtn, locationBtn);
        root.getChildren().addAll(title, listView, actions);

        initialized = true;
    }

    @Override
    public Parent getRoot() {
        lazyInit();
        return root;
    }

    // Show details view
    private void showDetails() {
        DetailsController detailsController = new DetailsController(stateMachine, this);
        stateMachine.changeState(detailsController);
    }

    // Show popup view
    private void showPopUp() {
        PopUpController popUpController = new PopUpController(stateMachine, this);
        stateMachine.changeState(popUpController);
    }

    // Navigate to location screen
    private void goToLocation() {
        // Use the cached controller if available
        LocationController locationController =
                (LocationController) stateMachine.getController(LocationController.class);
        stateMachine.changeState(locationController);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data if needed when entering this state
        if (listView != null && listView.getItems().isEmpty()) {
            Platform.runLater(() -> {
                listView.getItems().addAll("Product 1", "Product 2", "Product 3");
            });
        }
    }
}