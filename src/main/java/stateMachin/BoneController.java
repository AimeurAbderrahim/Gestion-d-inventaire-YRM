package stateMachin;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BoneController extends BaseController {
    private VBox root;

    public BoneController(ControllerStateMachine stateMachine) {
        super(stateMachine);
        createView();
    }

    private void createView() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        // Header
        Label title = new Label("Bone Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Content
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll("Bone 1", "Bone 2", "Bone 3");
        VBox.setVgrow(listView, Priority.ALWAYS);

        // Action buttons
        HBox actions = new HBox(10);
        Button detailsBtn = new Button("Details");
        detailsBtn.setOnAction(e -> showDetails());

        Button popupBtn = new Button("Pop Up");
        popupBtn.setOnAction(e -> showPopUp());

        Button locationBtn = new Button("Go to Location");
        locationBtn.setOnAction(e -> stateMachine.changeState(new LocationController(stateMachine)));

        Button fournisurBtn = new Button("Go to Fournisur");
        fournisurBtn.setOnAction(e -> stateMachine.changeState(new FournisurController(stateMachine)));

        actions.getChildren().addAll(detailsBtn, popupBtn, locationBtn, fournisurBtn);

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
        System.out.println("Bone method called with: " + type);
    }
}
