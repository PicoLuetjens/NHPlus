package de.hitec.nhplus.application;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.controller.LoginFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The <code>LoginForm</code> contains the initialization of the <code>LoginFormView</code>. This includes the window setup and window settings.
 */
public class LoginForm extends Application {
	private static Stage loginStage;

	/**
	 * When <code>start()</code> gets called, it sets the stage and calls the {@link #loginWindow(Stage)}} function.
	 * *
	 * @param primaryStage Stage that defines the loginStage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		loginStage = primaryStage;
		loginWindow(primaryStage);
	}

	/**
	 * When this function gets called, it sets up the login window.
	 * *
	 * @param primaryStage Stage that defines the loginStage assigned with the window.
	 */
	public void loginWindow(Stage primaryStage) {
		try {
			LoginFormController control = new LoginFormController();
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/LoginFormView.fxml"));
			StackPane pane = loader.load();
			Scene scene = new Scene(pane);
			loginStage.getIcons().add(new Image("icon.png"));
			loginStage.setTitle("Login");
			loginStage.setScene(scene);
			loginStage.setResizable(false);
			loginStage.show();
			control.clickButton();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * When this function is called it closes the login window.
	 */
	public static void closeStage() {
		loginStage.close();
	}
}