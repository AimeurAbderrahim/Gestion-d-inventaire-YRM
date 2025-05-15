package stateMachin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {
	private ControllerStateMachine stateMachine;

	@Override
	public void start(Stage stage) {
		// Apply hardware acceleration hints
		System.setProperty("prism.forceGPU", "true");
		System.setProperty("javafx.animation.fullspeed", "true");

		// Preload commonly used fonts to avoid font loading delay
		Platform.runLater(() -> {
			Font.loadFont(getClass().getResourceAsStream("/fonts/System.font"), 10);
		});

		stage.setTitle("Controller State Machine");
		stage.setWidth(600);
		stage.setHeight(400);

		stateMachine = new ControllerStateMachine(stage);

		// Start with the Product state
		ProductController initialState = new ProductController(stateMachine);
		stateMachine.changeState(initialState);

		stage.show();
	}

	public static void main(String[] args) {
		// Enable these VM options for better performance
		// -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -Djavafx.preloader=true
		launch(args);
	}

	@Override
	public void stop() {
		// Clean up resources when the application closes
		Platform.exit();
	}

}
