package com.lab;

import javafx.fxml.FXMLLoader;
import java.io.IOException;
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
	public void start(Stage stage)
	{
		Parent home = null;
		try{
			home = FXMLLoader.load(getClass().getResource("home.fxml"));
		}catch(IOException e){
			System.out.print("[" + Lib.RED + "ERROR" + Lib.RESET + "]" + " file home.fxml non trovato oppure malformato: " + Lib.RESET);
			e.printStackTrace();
			return;
		}

		//if(home instanceof javafx.scene.layout.Pane) Lib.drawGridLines((javafx.scene.layout.Pane) home);

		Scene scene = new Scene(home, 1280, 700);
		scene.setFill(Color.TRANSPARENT);

		Image icon = new Image(getClass().getResource("img/logo.png").toExternalForm());
		stage.getIcons().add(icon);

		stage.setTitle("TheKnife");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

}