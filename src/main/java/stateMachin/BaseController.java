package stateMachin;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public abstract class BaseController {
    protected ControllerStateMachine stateMachine;
    protected Parent root;
    protected Scene scene;

    public BaseController() {
        // Default constructor for FXML loader
        System.out.println(getClass().getSimpleName() + " created without state machine");
    }

    public BaseController(ControllerStateMachine stateMachine) {
        this.stateMachine = stateMachine;
        System.out.println(getClass().getSimpleName() + " created with state machine");
    }

    public void initStateMachine(ControllerStateMachine stateMachine) {
        this.stateMachine = stateMachine;
        System.out.println(getClass().getSimpleName() + " received state machine reference");
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    // Called when this controller becomes active
    public void onEnter() {
        System.out.println(getClass().getSimpleName() + " entered");

        // Ensure root is properly attached to scene
        if (root != null && scene != null) {
            // Force a layout pass
            root.applyCss();
            root.layout();
        }
    }
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
    // Called when switching away from this controller
    public void onExit() {
        System.out.println(getClass().getSimpleName() + " exited");
    }
}