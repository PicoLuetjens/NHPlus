package de.hitec.nhplus;

import de.hitec.nhplus.application.LoginForm;
import de.hitec.nhplus.application.MainWindow;
import de.hitec.nhplus.datastorage.ConnectionBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

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