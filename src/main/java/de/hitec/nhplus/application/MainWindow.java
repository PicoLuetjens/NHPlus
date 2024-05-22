package de.hitec.nhplus.application;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.ConnectionBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {
	private Stage primaryStage;

	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		mainWindow();
	}

	public void mainWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/MainWindowView.fxml"));
			BorderPane pane = loader.load();
			Scene scene = new Scene(pane);
			primaryStage.getIcons().add(new Image("icon.png"));
			this.primaryStage.setTitle("NHPlus");
			this.primaryStage.setScene(scene);
			this.primaryStage.setResizable(true);
			this.primaryStage.show();
			this.primaryStage.setOnCloseRequest(event -> {
				ConnectionBuilder.closeConnection();
				Platform.exit();
				System.exit(0);
			});
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}