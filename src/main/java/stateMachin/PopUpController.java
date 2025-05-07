package stateMachin;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PopUpController extends BaseController {
    private VBox root;
    private BaseController parentController;

    public PopUpController(ControllerStateMachine stateMachine, BaseController parentController) {
        super(stateMachine);
        this.parentController = parentController;
        createView();
    }

    private void createView() {
        root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999; -fx-border-width: 1px;");

        Label title = new Label("Pop Up");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label message = new Label("This is a popup from " + parentController.getClass().getSimpleName());

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stateMachine.changeState(parentController));

        root.getChildren().addAll(title, message, closeBtn);
    }

    @Override
    public Parent getRoot() {
        return root;
    }
}

