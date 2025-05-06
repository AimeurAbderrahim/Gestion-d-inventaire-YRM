package stateMachin;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ControllerStateMachine {
    private Stage stage;
    private BaseController currentController;
    private BaseController previousController;

    // Cache for controllers to avoid recreating them
    private Map<Class<? extends BaseController>, BaseController> controllerCache = new HashMap<>();

    // Scene cache to avoid recreating scenes
    private Scene scene;

    public ControllerStateMachine(Stage stage) {
        this.stage = stage;
        // Create a single scene that will be reused
        this.scene = new Scene(new javafx.scene.layout.StackPane());
    }

    // Cache-aware controller factory
    @SuppressWarnings("unchecked")
    public <T extends BaseController> T getController(Class<T> controllerClass) {
        return (T) controllerCache.computeIfAbsent(controllerClass, clazz -> {
            try {
                // Assumes each controller has a constructor that accepts ControllerStateMachine
                return clazz.getConstructor(ControllerStateMachine.class).newInstance(this);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    // Main method for changing between states/controllers with performance optimizations
    public void changeState(BaseController newController) {
        if (currentController != null) {
            currentController.onExit();
        }

        previousController = currentController;
        currentController = newController;

        // Reuse the scene but change the root
        scene.setRoot(currentController.getRoot());

        // Only set the scene if it's not already set
        if (stage.getScene() != scene) {
            stage.setScene(scene);
        }

        currentController.onEnter();
    }

    public BaseController getCurrentController() {
        return currentController;
    }

    public BaseController getPreviousController() {
        return previousController;
    }

    // Go back to previous controller
    public void goBack() {
        if (previousController != null) {
            BaseController temp = currentController;
            currentController = previousController;
            previousController = temp;

            // Reuse the scene but change the root
            scene.setRoot(currentController.getRoot());
            currentController.onEnter();
        }
    }
}