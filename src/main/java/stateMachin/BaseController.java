package stateMachin;

import javafx.scene.Parent;

public abstract class BaseController {
    protected ControllerStateMachine stateMachine;
    protected Parent root;

    public BaseController(ControllerStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    // Get the JavaFX root node for this controller
    public Parent getRoot() {
        if (root == null) {
            loadFXML();
        }
        return root;
    }

    // Method to load the FXML file (to be implemented by subclasses)
    protected void loadFXML() {
        // Default implementation does nothing
        // Override in subclasses that use FXML
    }

    // Called when entering this state
    public void onEnter() {
        System.out.println("Entering " + getClass().getSimpleName());
    }

    // Called when exiting this state
    public void onExit() {
        System.out.println("Exiting " + getClass().getSimpleName());
    }
}