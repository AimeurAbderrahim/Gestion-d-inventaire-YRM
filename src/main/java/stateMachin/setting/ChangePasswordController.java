package stateMachin.setting;

import db.configuration.ConfigDatabase;
import db.utils.AuthunticationVerification;
import db.java.CompteDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import testpackage.model.core.Compte;


public class ChangePasswordController {
	@FXML private PasswordField currentPasswordField;
	@FXML private PasswordField newPasswordField;
	@FXML private PasswordField confirmPasswordField;

	private Compte currentUser;

	public void setCurrentUser(Compte user) {
		this.currentUser = user;
	}

	@FXML
	private void changePassword() {
		String current = currentPasswordField.getText();
		String newPass = newPasswordField.getText();
		String confirm = confirmPasswordField.getText();

		if(!newPass.equals(confirm)) {
			showAlert("New passwords do not match!");
			return;
		}

		ConfigDatabase db = null;
		try {
			db = new ConfigDatabase();
			db.getConnection();
			CompteDatabase compteDB = new CompteDatabase(db, null, null);

			AuthunticationVerification check = null;
			if(this.currentUser != null){
				check = new AuthunticationVerification(this.currentUser.getNom_utilisateur() , current);
			}else{
				System.err.println("this.currentUser is null");
			}

			if(check == null && !check.checkAuth()) {
				showAlert("Current password is incorrect!");
				return;
			}

			if(check.newPassword(newPass))
				showAlert("Password changed successfully!");
			close();

		} catch (Exception e) {
			showAlert("Error changing password: " + e.getMessage());
		}

		if(db != null){
			try{
				db.closeConnection();
			}catch(Exception err){
				showAlert(err.getMessage());
			}
		}
	}

	@FXML
	private void close() {
		((Stage) currentPasswordField.getScene().getWindow()).close();
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(message);
		alert.showAndWait();
	}
}
