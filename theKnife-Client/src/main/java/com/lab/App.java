package com.lab;

import javafx.fxml.FXMLLoader;
import java.io.IOException;

import com.lab.server.ServerConnection;
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
		boolean connected = ServerConnection.connect("localhost");
		if(connected) launch(args);
		else {
			System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "]" + " Impossibile avviare il client, controllare la conessione con il server");
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
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "]" + " file page.fxml non trovato: ");
      e.printStackTrace();
      return;
    }

		//if(root instanceof Pane) drawGridLines((Pane) root);

		Scene scene = new Scene(root, 1280, 700);
		scene.setFill(Color.TRANSPARENT);

		Image icon = new Image(getClass().getResource("/com/lab/img/logo.png").toExternalForm());
		stage.getIcons().add(icon);
		
		stage.setTitle("TheKnife");
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.show();
	}

	private static void drawGridLines(Pane root)
  {
    double width = root.getPrefWidth();
    double height = root.getPrefHeight();

    for(int i=0; i<width; i+=40) {
      Line lineV = new Line(i, 0, i, height);
      lineV.setStroke(Color.RED);
      lineV.setStrokeWidth(1);
      lineV.setOpacity(0.3);
      root.getChildren().add(lineV);
    }

    for(int i=0; i<height; i+=50) {
      Line lineH = new Line(0, i, width, i);
      lineH.setStroke(Color.RED);
      lineH.setStrokeWidth(1);
      lineH.setOpacity(0.3);
      root.getChildren().add(lineH);
    }

    Line lineC = new Line(0, height/2, width, height/2);
    lineC.setStroke(Color.BLUE);
    lineC.setStrokeWidth(3);
    lineC.setOpacity(0.3);
    root.getChildren().add(lineC);

    lineC = new Line(width/2, 0, width/2, height);
    lineC.setStroke(Color.BLUE);
    lineC.setStrokeWidth(3);
    lineC.setOpacity(0.5);
    root.getChildren().add(lineC);
  }
}