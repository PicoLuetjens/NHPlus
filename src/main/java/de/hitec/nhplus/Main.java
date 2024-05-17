package de.hitec.nhplus;

import de.hitec.nhplus.application.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class Main extends Application {
	private Stage primaryStage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		LoginForm login = new LoginForm();
		login.start(primaryStage);
	}


	public static void main(String[] args) {

		final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		final URL imageResource = Main.class.getClassLoader().getResource("icon.png");
		final Image image = defaultToolkit.getImage(imageResource);
		final Taskbar taskbar = Taskbar.getTaskbar();
		try {
			taskbar.setIconImage(image);
		}
		catch (final UnsupportedOperationException e) {
			System.out.println("The os does not support: 'taskbar.setIconImage'");
		}
		catch (final SecurityException e) {
			System.out.println("There was a security exception for: 'taskbar.setIconImage'");
		}

		launch(args);
	}

}