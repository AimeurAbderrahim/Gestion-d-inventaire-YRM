package stateMachin;

import db.configuration.ConfigDatabase;
import db.errors.CloseConnectionException;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private ControllerStateMachine stateMachine;
    // private ConfigDatabase db;


    // public ConfigDatabase getDb() {
    //     return db;
    // }

    public MainApp() {
        ConfigDatabase db = null;
        try{
			db = new ConfigDatabase();
			db.getConnection();
        }catch(ConnectionFailedException error){
			System.out.println(error.getMessage());
			System.exit(0);
		}catch(LoadPropertiesException err){
			System.out.println(err.getMessage());
			System.exit(0);
		}
    }

    @Override
    public void start(Stage stage) {
		
        // Apply hardware acceleration hints
        System.setProperty("prism.forceGPU", "true");
        System.setProperty("javafx.animation.fullspeed", "true");

        // Debug startup
        System.out.println("Starting application...");

        stage.setTitle("Resource Management System");
        stage.setFullScreen(true);
        stage.setMinWidth(1280);
        stage.setMinHeight(720);


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
        // try {
        //     db.closeConnection();
        // } catch(CloseConnectionException err){
		// 	System.out.println(err.getMessage());
		// 	System.exit(0);
		// }
        // Clean up resources when the application closes
        System.out.println("Application shutting down...");
        Platform.exit();
    }
}