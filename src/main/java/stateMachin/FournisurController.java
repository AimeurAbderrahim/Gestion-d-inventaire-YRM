package stateMachin;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FournisurController extends BaseController {
    private VBox root;

    public FournisurController(ControllerStateMachine stateMachine) {
        super(stateMachine);
        createView();
    }

    private void createView() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        // Header
        Label title = new Label("Fournisur Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Content
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll("Supplier 1", "Supplier 2", "Supplier 3");
        VBox.setVgrow(listView, Priority.ALWAYS);

        // Action buttons
        HBox actions = new HBox(10);
        Button detailsBtn = new Button("Details");
        detailsBtn.setOnAction(e -> showDetails());

        Button popupBtn = new Button("Pop Up");
        popupBtn.setOnAction(e -> showPopUp());

        Button boneBtn = new Button("Go to Bone");
        boneBtn.setOnAction(e -> stateMachine.changeState(new BoneController(stateMachine)));

        actions.getChildren().addAll(detailsBtn, popupBtn, boneBtn);

        root.getChildren().addAll(title, listView, actions);
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    // Show details view
    private void showDetails() {
        stateMachine.changeState(new DetailsController(stateMachine, this));
    }

    // Show popup view
    private void showPopUp() {
        stateMachine.changeState(new PopUpController(stateMachine, this));
    }

    // Example method implementation
    public void method(String type) {
        System.out.println("Fournisur method called with: " + type);
    }
}
