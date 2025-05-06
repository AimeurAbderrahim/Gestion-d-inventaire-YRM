package stateMachin;

import javafx.scene.Parent;

public abstract class BaseController {
    protected ControllerStateMachine stateMachine;

    public BaseController(ControllerStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    // Get the JavaFX root node for this controller
    public abstract Parent getRoot();

    // Called when entering this state
    public void onEnter() {
        System.out.println("Entering " + getClass().getSimpleName());
    }

    // Called when exiting this state
    public void onExit() {
        System.out.println("Exiting " + getClass().getSimpleName());
    }
}
