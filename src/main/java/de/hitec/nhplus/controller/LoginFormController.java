package de.hitec.nhplus.controller;

import de.hitec.nhplus.application.MainWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginFormController {

	@FXML
	private TextField user;
	@FXML
	private TextField password;
	@FXML
	private Button loginButton;
	@FXML
	private Label responseLabel;

	private Stage bufferStage;
	private static final String CORRECT_USERNAME = "test";
	private static final String CORRECT_PASSWORD = "pass123";

	public void initialize() {
	}

	public void clickButton() {

		System.out.println("foo");
		ActionEvent event = new ActionEvent();
		Button loginButton = new Button();
		loginButton.setOnAction(e -> {
			loginValidation(event);
		});
	}

	public void loginValidation(ActionEvent event) {

		String enteredUsername = user.getText();
		String enteredPassword = password.getText();
		System.out.println("step 1");
		if (enteredUsername.equals(CORRECT_USERNAME) && enteredPassword.equals(CORRECT_PASSWORD)) {
			responseLabel.setText("Login successful!");
			Stage stage = new Stage();
			MainWindow window = new MainWindow();
			window.start(stage);
		} else {
			responseLabel.setText("Login Failed");
			System.out.println("foo2");
		}
	}
}
