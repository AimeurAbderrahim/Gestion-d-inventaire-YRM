package stateMachin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerStateMachine {
    private Stage primaryStage;
    private Map<EnumScenes, Scene> scenes;
    private Map<EnumScenes, BaseController> controllers;
    private BaseController currentController;

    public ControllerStateMachine(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.scenes = new HashMap<>();
        this.controllers = new HashMap<>();

        // Initialize all scenes
        initializeScenes();
    }

    private void initializeScenes() {
        try {
            loadScene(EnumScenes.Login, "/stateMachin/pages/LoginPage.fxml");

            // Load other scenes
            loadScene(EnumScenes.Bone, "/stateMachin/pages/BoneView.fxml");
            loadScene(EnumScenes.Location, "/stateMachin/pages/Emplacement.fxml");
            loadScene(EnumScenes.Products, "/stateMachin/pages/ProductView.fxml");
            loadScene(EnumScenes.Fournisur, "/stateMachin/pages/FournisurView.fxml");
            loadScene(EnumScenes.Welcome, "/stateMachin/pages/WelcomePage.fxml");

            System.out.println("All scenes initialized successfully");
        } catch (IOException e) {
            System.err.println("Failed to initialize scenes: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void loadScene(EnumScenes sceneType, String path) throws IOException {
        try {
            System.out.println("Loading scene: " + sceneType + " from path: " + path);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();
            Scene scene = new Scene(root, screenWidth, screenHeight);
            scenes.put(sceneType, scene);

            // Get the controller and store it
            BaseController controller = fxmlLoader.getController();

            // Set the state machine reference in the controller
            controller.initStateMachine(this);

            // Store the controller for future reference
            controllers.put(sceneType, controller);

            System.out.println("Loaded scene: " + sceneType + " with controller: " + controller.getClass().getSimpleName());
        } catch (IOException e) {
            System.err.println("Error loading scene " + sceneType + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void changeScene(EnumScenes sceneType, ActionEvent event) {
        try {
            // Get the stage from the event source
            primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // If we have a current controller, call its onExit method
            if (currentController != null) {
                currentController.onExit();
            }

            // Update current controller
            currentController = controllers.get(sceneType);

            // Call onEnter for the new controller
            if (currentController != null) {
                currentController.onEnter();
            } else {
                System.err.println("WARNING: No controller found for scene " + sceneType);
            }

            // Get the next scene
            Scene nextScene = scenes.get(sceneType);

            // Set the scene on the stage
            primaryStage.setScene(nextScene);

            // Force layout update
            nextScene.getRoot().layout();

            // Request focus to ensure UI updates
            nextScene.getRoot().requestFocus();

            // Force stage to update
            primaryStage.sizeToScene();
            primaryStage.show();

           /* // Force a UI refresh with a tiny window size change
            javafx.application.Platform.runLater(() -> {
                primaryStage.setWidth(primaryStage.getWidth() + 1);
                javafx.application.Platform.runLater(() -> {
                    primaryStage.setWidth(primaryStage.getWidth() - 1);
                });
            });
*/
            System.out.println("Changed scene to: " + sceneType);
        } catch (Exception e) {
            System.err.println("Error changing scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get a scene by type
    public Scene getScene(EnumScenes sceneType) {
        return scenes.get(sceneType);
    }

    // Enter a scene without an action event (used for initial scene)
    public void enterScene(EnumScenes sceneType) {
        // If we have a current controller, call its onExit method
        if (currentController != null) {
            currentController.onExit();
        }

        // Update current controller
        currentController = controllers.get(sceneType);

        // Call onEnter for the new controller
        if (currentController != null) {
            currentController.onEnter();
        } else {
            System.err.println("WARNING: No controller found for scene " + sceneType);
        }

        // Force a layout pass and repaint
        Scene scene = scenes.get(sceneType);
        if (scene != null && scene.getRoot() != null) {
            scene.getRoot().layout();
        }

        // Request UI refresh
        javafx.application.Platform.runLater(() -> {
            primaryStage.sizeToScene();
        });

        System.out.println("Entered scene: " + sceneType);
    }
}
