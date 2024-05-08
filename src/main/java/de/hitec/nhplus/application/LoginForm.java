package de.hitec.nhplus.application;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.controller.LoginFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginForm extends Application {
	private Stage loginStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.loginStage = primaryStage;
		loginWindow(primaryStage);
	}

	public void loginWindow(Stage primaryStage) {
		try {
			LoginFormController control = new LoginFormController();
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/LoginFormView.fxml"));
			StackPane pane = loader.load();
			Scene scene = new Scene(pane);
			this.loginStage.setTitle("Login");
			this.loginStage.setScene(scene);
			this.loginStage.setResizable(false);
			this.loginStage.show();
			control.clickButton();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
