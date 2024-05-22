package de.hitec.nhplus.controller;

import de.hitec.nhplus.application.LoginForm;
import de.hitec.nhplus.application.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginFormController {

	@FXML
	private TextField user;
	@FXML
	private PasswordField password;
	@FXML
	private Button loginButton;
	@FXML
	private Label responseLabel;

	private static final String CORRECT_USERNAME = "test";
	private static final String CORRECT_PASSWORD = "pass123";
	private static final String CORRECT_ADMIN_USERNAME = "admin";
	private static final String CORRECT_ADMIN_PASSWORD = "admin123";

	public void initialize() {
	}

	public void clickButton() {
		ActionEvent event = new ActionEvent();
		Button loginButton = new Button();
		loginButton.setOnAction(e -> {
			loginValidation(event);
		});
	}

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
