/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.user;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import com.lab.model.Restaurant;
import com.lab.model.Session;
import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * DetailsUserController gestisce l'interfaccia relativa ai dettagli di un ristorante per l'utente.
 * <p>
 * Permette di visualizzare maggiori informazioni relative ad un ristorante, di visualizzare le recensioni relative ad esse e anche di recensirlo.
 * </p> 
 */
public class DetailsUserController {
  
  @FXML private Text name_L;
  @FXML private Label address_L;
  @FXML private Label price_L;
  @FXML private Label delivery_L;
  @FXML private Label booking_L;
  @FXML private Label cuisine_L;
  @FXML private ScrollPane listContainer;
	@FXML private VBox listOfComments;
  @FXML private Button reviewButton;
  @FXML private Label emptyLabel;

  private Restaurant restaurant;
  
  /**
   * Imposta il valore dei componenti grafici delle informazioni del ristorante.
   * <p>
   * Dal ristorante ottenuto dalla lista degli argomenti, ne prelega le informazioni e aggiorna la grafica testuale delle relative informazioni.
   * Inoltre si occupa anche di chiamare il metodo {@link #loadReviews(int)} per caricare la lista delle recensioni relative al ristorante. 
   * </p>
   * 
   * @param r Il ristorante da cui reperire i dati aggiuntivi.
   */
  public void setDetails(Restaurant r)
	{
    restaurant = r;

    name_L.setText(r.getName());

    String fullAddress = r.getAddress();
    String shortAddress = fullAddress;
    if(fullAddress.contains(",")){
      String[] split = fullAddress.split(",");
      if(split.length >= 2) shortAddress = split[0] + ", " + split[1]; 
    }
    address_L.setText(shortAddress);

    price_L.setText((r.getPrice() == null || r.getPrice().isEmpty()) ? "..." : r.getPrice());
    delivery_L.setText((r.getDelivery() == null || r.getDelivery().isEmpty()) ? "No" : r.getDelivery());
    booking_L.setText((r.getBooking() == null || r.getBooking().isEmpty()) ? "No" : r.getBooking());
    cuisine_L.setText(r.getCuisine());

		loadReviews(r.getId());

    if(Session.getCurrentUser() == null) {
      reviewButton.setVisible(false);
      reviewButton.setManaged(false);
    }
  }

  /**
   * Gestisce l'evento di ritorno indietro dalla visualizzazione dei dettagli del ristorante da parte di un utente.
   * <p>
   * Chiama il metodo {@link UserHomeController#closeDetails()} che si occupa della chiusura della sovrafinestra dei dettagli.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Indietro.
   */
  @FXML
  public void backClicked(ActionEvent event)
	{
    System.out.println("[" + StringColor.GREEN + "ACTION] " + StringColor.RESET + "Details view closed");
    UserHomeController.getInstance().closeDetails();
  }

  /**
   * Gestisce l'evento che permette all'utente di lasciare una recensione ad un ristorante.
   * <p>
   * Chiama il metodo {@link UserHomeController#openWriteComment(Restaurant)} che si occupa dell'apertura della finestra di dialogo che permette di eseguire la pratica per l'inserimento di una recensione.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Recensisci.
   */
	@FXML
  public void reviewClicked(ActionEvent event)
	{
    System.out.println("[" + StringColor.GREEN + "ACTION] " + StringColor.RESET + "Review clicked");

    //String name = name_L.getText();
    UserHomeController.getInstance().openWriteComment(restaurant);
  }

  /**
   * Ottiene le recensioni del ristorante.
   * <p>
   * Il thread grafico perde momentaneamente il controllo, viene generato un thread asincrono in background che si occupa di inoltrare la richiesta dei dati al server remoto.
   * Una volta ricevuta una risposta, il thread grafico riprende il controllo.
   * Chiama il metodo {@link ReviewsRowController#setReviews(Stringp[])} che si occupa di collegare la visualizzazione grafica della singola recensione e i dati ottenuti.
   * </p>
   * 
   * @param restaurantId id del ristorante da cui caricare le recensioni.
   */
  private void loadReviews(int restaurantId)
  {
    listOfComments.getChildren().clear();

    CompletableFuture.supplyAsync(() -> {
      try{
        return ServerConnection.getServer().getRestaurantReviews(restaurantId);
      } catch (RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta recensioni ristorante");
        return null;
      }
    }).thenAccept(reviews -> {
      Platform.runLater(() -> {
        if(reviews == null) {
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta recensioni ristorante");
          emptyLabel.setText("Errore di connessione con il server");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        } else if(reviews.isEmpty()) {
          emptyLabel.setText("Nessuna recensione trovata");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        } else {
          emptyLabel.setVisible(false);
          emptyLabel.setManaged(false);
          for(String[] r : reviews) {
            try{
              FXMLLoader loader = new FXMLLoader(com.lab.App.class.getResource("/com/lab/fxml/user/reviewsRow.fxml"));
              VBox row = loader.load();

              ReviewsRowController controller = loader.getController();
              controller.setReview(r);

              listOfComments.getChildren().add(row);
            }catch (IOException e) {
              System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento recensioni ristorante");
              e.printStackTrace();
            }
          }
        }
      });
    });
  }

  /**
   * Si occupa di richiamare il metodo {@link #loadReviews(int)} per aggiornare la lista delle recensioni dopo una modifica.
   */
  public void refreshReviews()
  {
    if (restaurant != null) loadReviews(restaurant.getId());
  }
}
