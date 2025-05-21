package stateMachin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private ControllerStateMachine stateMachine;

    @Override
    public void start(Stage stage) {
        // Apply hardware acceleration hints
        System.setProperty("prism.forceGPU", "true");
        System.setProperty("javafx.animation.fullspeed", "true");

        // Debug startup
        System.out.println("Starting application...");

        stage.setTitle("Resource Management System");
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        try {
            System.out.println("Creating state machine...");
            stateMachine = new ControllerStateMachine(stage);

            // Start with the Login scene
            System.out.println("Setting initial scene to Login");

            // Access the login scene from the state machine
            Scene loginScene = stateMachine.getScene(EnumScenes.Login);
            stage.setScene(loginScene);

            // Ensure the scene is properly laid out
            loginScene.getRoot().layout();

            // Show the stage
            stage.show();

            // Notify the controller that we're entering this scene
            stateMachine.enterScene(EnumScenes.Login);

            // Force a UI refresh after everything is set up
            Platform.runLater(() -> {
                stage.sizeToScene();
            });

            System.out.println("Application started successfully");
        } catch (Exception e) {
            System.err.println("Error during application startup: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        // Enable these VM options for better performance
        // -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -Djavafx.preloader=true
        launch(args);
    }

    @Override
    public void stop() {
        // Clean up resources when the application closes
        System.out.println("Application shutting down...");
        Platform.exit();
    }
}