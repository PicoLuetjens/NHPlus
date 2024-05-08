package de.hitec.nhplus;

import de.hitec.nhplus.application.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	private Stage primaryStage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		LoginForm login = new LoginForm();
		login.start(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}

}