/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.restaurateur;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import java.util.List;

import com.lab.App;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.model.Restaurant;
import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * DetailsRestaurateurController Gestisce l'interfaccia per la rappresentazione dei dettagli di un ristorante per il ristoratore.
 * <p>
 * Questa classe si occupa di ottenere i commenti di un ristoratore e di impostarli per la loro visualizzazione.
 * </p>
 */
public class DetailsRestaurateurController {

  @FXML private VBox list;
  @FXML private ScrollPane listContainer;
  @FXML private Label emptyLabel;

  private Restaurant currentRestaurant;

  /**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Rende invisibile il titolo della pagina
   * Rende invisibile il bottone Indietro della toolbar
   * Rende visibile il bottone Esci della toolbar
   * </p>
   */
  @FXML
  public void initialize()
  {
    PageController.showTitle(false); 
    ToolbarController.showBackButton(false);
    ToolbarController.showLeftSide(false, false, true);
  }

  /**
   * Imposta il ristorante corrente e carica le recensioni relative ad esso.
   */
  public void setRestaurant(Restaurant r)
  {
    currentRestaurant = r;
    fillReviews();
  }

  /**
   * Richiede la lista delle recensioni al server remoto, tramite un thread in background. 
   * Una volta ricevuta la risposta, riprendere il controllo il thread grafico, interrotto precedentemente per non generare innumerevoli chiamate al server,
   * La lista delle recensioni viene spacchettata, impostati i singoli dati e aggiunti alla lista di recensioni grafica.
   */
  public void fillReviews()
  {
    list.getChildren().clear();
    if(currentRestaurant == null) return;

    CompletableFuture.supplyAsync(() -> {
      try{
        return ServerConnection.getServer().getRestaurateurReviews(currentRestaurant.getId());
      } catch (RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta recensioni del proprio ristorante");
        return null;
      }
    }).thenAccept(reviews -> {
      Platform.runLater(() -> {
        if(reviews == null) {
          emptyLabel.setText("Errore di connessione con il server");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        } else if(reviews.isEmpty()) {
          emptyLabel.setText("Nessuna recensione presente al momento");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        } else {
          emptyLabel.setVisible(false);
          emptyLabel.setManaged(false);
          for(String[] r : reviews) {
            try{
              FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/restaurateur/yourReviews.fxml"));
              HBox row = loader.load();

              YourReviewsController controller = loader.getController();
              String[] fullData = {r[0], r[1], r[2], r[3] != null ? r[3] : "", r[4], String.valueOf(currentRestaurant.getId())};
              controller.setReviewData(fullData);

              list.getChildren().add(row);
            }catch(IOException e) {
              System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento delle recensioni del proprio ristorante");
              e.printStackTrace();
            }
          }
        }
      });
    });
  }

  /**
   * Gestisce l'evento torna indietro dalla schermata di visualizzazione dei dettagli del ristorante per il ristoratore.
   * <p>
   * Nel caso il ristoratore non volesse piu' visualizzare le recensioni, gli e' permesso tornare indietro alla visualizzazione dei propri ristoranti.
   * Questo viene fatto chiamando il metodo {@link RestaurateurHomeController#closeDetails()}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Indietro.
   */
  @FXML public void backClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Back button clicked");
    RestaurateurHomeController.getInstance().closeDetails();
  }
}
