package de.hitec.nhplus.controller;

import de.hitec.nhplus.application.LoginForm;
import de.hitec.nhplus.application.MainWindow;
import de.hitec.nhplus.datastorage.TreatmentDao;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The <code>LoginFormController</code> contains the entire logic of the LoginFormView. It determines which data is displayed and how to react to events.
 */
public class LoginFormController {

	@FXML
	private TextField user;
	@FXML
	private PasswordField password;
	@FXML
	private Button loginButton;
	@FXML
	private Label responseLabel;

	private Stage bufferStage;
	private static final String CORRECT_USERNAME = "test";
	private static final String CORRECT_PASSWORD = "pass123";
	private static final String CORRECT_ADMIN_USERNAME = "admin";
	private static final String CORRECT_ADMIN_PASSWORD = "admin123";

	public void initialize() {
	}

	/**
	 * Gets executed when the login button is clicked.
	 */
	public void clickButton() {
		ActionEvent event = new ActionEvent();
		Button loginButton = new Button();
		loginButton.setOnAction(e -> {
			loginValidation(event);
		});
	}

	/**
	 * Validates the login data provided.
	 * @param event The event that triggered the loginbutton.
	 */
	public void loginValidation(ActionEvent event) {

		String enteredUsername = user.getText();
		String enteredPassword = password.getText();
		if(CORRECT_USERNAME.equals(enteredUsername) && CORRECT_PASSWORD.equals(enteredPassword)){
			MainWindowController.IS_ADMIN = false;
			responseLabel.setText("Login successful!");
			Stage stage = new Stage();
			MainWindow window = new MainWindow();
			LoginForm.closeStage();
			window.start(stage);
		}
		else if(CORRECT_ADMIN_USERNAME.equals(enteredUsername) && CORRECT_ADMIN_PASSWORD.equals(enteredPassword)){
			MainWindowController.IS_ADMIN = true;
			responseLabel.setText("Admin Login successful!");
			Stage stage = new Stage();
			MainWindow window = new MainWindow();
			LoginForm.closeStage();
			window.start(stage);
		}
		else {
			responseLabel.setText("Login Failed");
		}
	}
}
