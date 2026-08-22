package com.lab;

import javafx.fxml.FXMLLoader;
import java.io.IOException;

import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
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
      root = FXMLLoader.load(App.class.getResource("/com/lab/fxml/ipConfig/ipConfig.fxml"));
    } catch(IOException e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "]" + " file ipConfig.fxml non trovato");
      System.exit(1);
    }

		Scene scene = new Scene(root, 640, 350);
		scene.setFill(Color.TRANSPARENT);

		Image icon = new Image(getClass().getResource("/com/lab/img/logo.png").toExternalForm());
		stage.getIcons().add(icon);
		
		stage.setTitle("TheKnife - Connect");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
	}
}