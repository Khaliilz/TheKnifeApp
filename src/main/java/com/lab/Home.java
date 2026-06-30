package com.lab;

import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Home extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		Parent home = FXMLLoader.load(getClass().getResource("home.fxml"));
		Scene scene = new Scene(home, 1280, 700);
		Image icon = new Image(getClass().getResource("img/logo.png").toExternalForm());
		stage.getIcons().add(icon);
		scene.setFill(Color.TRANSPARENT);
		stage.setTitle("TheKnife");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

}