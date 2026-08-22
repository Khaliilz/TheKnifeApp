package com.lab.controller.ipConfig;

import com.lab.App;
import com.lab.network.ServerConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import com.lab.utility.ErrorContainer;

import java.util.concurrent.CompletableFuture;

public class IpConfigController {

  @FXML private TextField ipContent;
  @FXML private Button connectButton;
  @FXML private Label errorLabel;

  @FXML
  public void initialize()
	{
		ErrorContainer.resetBorder(ipContent);
  	errorLabel.setText("");
		connectButton.setOnAction(this::connectClicked);
  }

  @FXML
  public void connectClicked(ActionEvent event)
  {
  	String ip = ipContent.getText().trim();
    if(ip.isEmpty()) ip = "localhost";

    errorLabel.setText("");
    connectButton.setDisable(true);
    connectButton.setText("CONNESSIONE IN CORSO...");

    final String serverIP = ip;

    CompletableFuture.supplyAsync(() -> {
      return ServerConnection.connect(serverIP);
    }).thenAccept(connected -> {
      Platform.runLater(() -> {
        if(connected) loadMainApp(event);
        else {
          connectButton.setDisable(false);
          connectButton.setText("CONNETTITI");
					ErrorContainer.errorBorder(ipContent);
          errorLabel.setText("Impossibile raggiungere il Server su: " + serverIP);
        }
      });
    });
  }

  @FXML
  public void exitClicked(ActionEvent event)
	{
    System.exit(0);
  }

  private void loadMainApp(ActionEvent event)
	{
    try {
      Parent root = FXMLLoader.load(App.class.getResource("/com/lab/fxml/basic/page.fxml"));

			//if(root instanceof Pane) drawGridLines((Pane) root);

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			Scene scene = new Scene(root, 1280, 700);
			scene.setFill(Color.TRANSPARENT);
			
			stage.setTitle("TheKnife");
			stage.setScene(scene);
			stage.centerOnScreen();
    } catch(Exception ex) {
      ex.printStackTrace();
      errorLabel.setText("Errore durante il caricamento della homepage");
    }
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