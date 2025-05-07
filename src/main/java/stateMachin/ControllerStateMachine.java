package stateMachin;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ControllerStateMachine {
    private BaseController currentController;
    private Stage primaryStage;
    private Scene mainScene;

    // ✅ Correctly typed controller cache
    private final Map<Class<? extends BaseController>, BaseController> controllerCache = new HashMap<>();

    public ControllerStateMachine(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void changeState(BaseController newController) {
        if (newController == null) {
            throw new IllegalArgumentException("New controller cannot be null");
        }

        this.currentController = newController;

        if (currentController.getRoot() == null) {
            throw new IllegalStateException("Controller returned null root node");
        }

        try {
            if (mainScene == null) {
                mainScene = new Scene(currentController.getRoot());
                primaryStage.setScene(mainScene);
            } else {
                mainScene.setRoot(currentController.getRoot());
            }
        } catch (Exception e) {
            System.err.println("Failed to set scene root: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to change application state", e);
        }

        primaryStage.setTitle("YRM Inventory Management - " +
                currentController.getClass().getSimpleName().replace("Controller", ""));
    }

    public BaseController getCurrentController() {
        return currentController;
    }

    // ✅ Now works correctly with generics and avoids the wildcard issue
    public <T extends BaseController> T getController(Class<T> controllerClass) {
        return controllerClass.cast(controllerCache.computeIfAbsent(controllerClass, clazz -> {
            try {
                return clazz.getConstructor(ControllerStateMachine.class).newInstance(this);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate controller: " + clazz.getName(), e);
            }
        }));
    }
}
