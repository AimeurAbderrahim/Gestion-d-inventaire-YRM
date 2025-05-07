package stateMachin;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class DetailsController extends BaseController {
    private VBox root;
    private BaseController parentController;

    public DetailsController(ControllerStateMachine stateMachine, BaseController parentController) {
        super(stateMachine);
        this.parentController = parentController;
        createView();
    }

    private void createView() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        Label title = new Label("Details");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextArea details = new TextArea();
        details.setText("Details for " + parentController.getClass().getSimpleName());
        details.setPrefHeight(200);
        details.setEditable(false);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> stateMachine.changeState(parentController));

        root.getChildren().addAll(title, details, backBtn);
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
