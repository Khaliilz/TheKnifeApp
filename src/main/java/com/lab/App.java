package com.lab;

import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.Connection;

import com.lab.database.Database;
import com.lab.database.PopulateRestaurants;
import com.lab.server.ServerConnection;
import com.lab.utility.Lib;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

	public static void main(String[] args) {
		boolean connected = ServerConnection.connect("localhost");
		if(connected) launch(args);
		else {
			System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "]" + " Impossibile avviare il client, controllare la conessione con il server");
			System.exit(1);
		}
	}

	@Override
	public void start(Stage stage)
	{
		Parent root = null;
    try {
      root = FXMLLoader.load(App.class.getResource("/com/lab/fxml/basic/page.fxml"));
    } catch(IOException e) {
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "]" + " file page.fxml non trovato: ");
      e.printStackTrace();
      return;
    }

		//if(root instanceof javafx.scene.layout.Pane) Lib.drawGridLines((javafx.scene.layout.Pane) root);

		Scene scene = new Scene(root, 1280, 700);
		scene.setFill(Color.TRANSPARENT);

		Image icon = new Image(getClass().getResource("/com/lab/img/logo.png").toExternalForm());
		stage.getIcons().add(icon);
		
		stage.setTitle("TheKnife");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
	}
}