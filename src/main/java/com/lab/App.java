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

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		Parent root = null;
    try {
      root = FXMLLoader.load(getClass().getResource("page.fxml"));
    } catch(IOException e) {
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "]" + " file page.fxml non trovato: ");
      e.printStackTrace();
      return;
    }

		//if(root instanceof javafx.scene.layout.Pane) Lib.drawGridLines((javafx.scene.layout.Pane) root);

		Scene scene = new Scene(root, 1280, 700);
		scene.setFill(Color.TRANSPARENT);

		Image icon = new Image(getClass().getResource("img/logo.png").toExternalForm());
		stage.getIcons().add(icon);

		stage.setTitle("TheKnife");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
	}

}