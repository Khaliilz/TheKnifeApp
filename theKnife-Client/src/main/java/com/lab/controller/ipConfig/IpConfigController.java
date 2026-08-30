/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
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

/**
 * IpConfigController Gestisce l'interfaccia di collegamento al server remoto.
 * <p>
 * Questa classe si occupa di gestire la grafica che permette all'utente di inserire l'indirizzo del server remoto per effetturare il collegamento.
 * </p>
 */
public class IpConfigController {

  @FXML private TextField ipContent;
  @FXML private Button connectButton;
  @FXML private Label errorLabel;

	/**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Reimposta la corretta visualizzazione dello stile degli input dell'utente.
	 * Imposta la pressione del tasto invio all'evento {@link #connectClicked(ActionEvent)}
   * </p>
   */
  @FXML
  public void initialize()
	{
		ErrorContainer.resetBorder(ipContent);
  	errorLabel.setText("");
		connectButton.setOnAction(this::connectClicked);
  }

	/**
   * Gestisce l'evento di richiesta di connessione al server remoto.
   * <p>
   * Verifica che l'input non sia vuoto. In caso di errore, applica un bordo di segnalazione tramite {@link ErrorContainer}.
   * Se la validificazione va a buon fine, l'intefaccia viene temporaneamente disabilitata e viene avviato un thread in background per inviare la richiesta al server remoto.
   * Ricevuta la risposta, il controllo ritorna al thread grafico, e se le credenziali del server sono correte viene avviata la connessione e aperta la homepage tramite il metodo {@link #loadMainApp(ActionEvent)}.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Connettiti.
   */
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
    }).thenAccept(success -> {
      Platform.runLater(() -> {
        if(success) loadMainApp(event);
        else {
          connectButton.setDisable(false);
          connectButton.setText("CONNETTITI");
					ErrorContainer.errorBorder(ipContent);
          errorLabel.setText("Impossibile raggiungere il Server su: " + serverIP);
        }
      });
    });
  }

	/**
   * Gestisce l'evento di uscita dall'applicazione.
   * <p>
   * Nel caso l'utente non volesse piu' connettersi, viene eseguito la chiusura del programma
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Esci.
   */
  @FXML
  public void exitClicked(ActionEvent event)
	{
    System.exit(0);
  }

	/**
   * Gestisce l'evento di apertura della homepage principale dell'applicazione.
   * <p>
   * Ottiene la risorsa della homepage dall'indirizzo della schermata e la mostra.
   * </p>
   * 
   * @param event L'evento scatenato dallo stabilimento della connessione con il server.
   */
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

	/**
   * Gestisce il disegno delle linee di allineamento per le componenti grafiche.
   * <p>
   * Crea delle linee orizzontali e verticali per un miglior occhio relativo alla posizione di ogni componente.
   * </p>
   * 
   * @param root Schermata su cui disegnare le linee.
   */
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