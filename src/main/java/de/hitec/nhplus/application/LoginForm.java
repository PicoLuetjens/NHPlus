package de.hitec.nhplus.login;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginForm extends Preloader {
	private Stage preloaderStage;

	private static final String CORRECT_USERNAME = "admin";
	private static final String CORRECT_PASSWORD = "admin123";

	public void start(Stage primaryStage) {
		primaryStage.setTitle("Login Form");

		Label usernameLabel = new Label("Username:");
		Label passwordLabel = new Label("Password:");
		TextField usernameField = new TextField();
		PasswordField passwordField = new PasswordField();
		Label resultLabel = new Label();
		Button loginButton = new Button("Login");

		loginButton.setOnAction(event -> {
			String enteredUsername = usernameField.getText();
			String enteredPassword = passwordField.getText();
			if (enteredUsername.equals(CORRECT_USERNAME) && enteredPassword.equals(CORRECT_PASSWORD)) {
				resultLabel.setText("Login erfolgreich");
			} else {
				resultLabel.setText("Benutzer oder Password inkorrekt!");
			}
		});

		VBox root = new VBox(10);
		root.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, resultLabel);
		Scene scene = new Scene(root, 300, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("NHPlus login");
		primaryStage.show();
	}
}
