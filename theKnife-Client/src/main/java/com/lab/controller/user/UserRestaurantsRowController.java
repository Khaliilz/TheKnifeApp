/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.user;

import com.lab.utility.StringColor;

import java.rmi.RemoteException;
import javafx.application.Platform;
import java.util.concurrent.CompletableFuture;

import com.lab.model.Restaurant;
import com.lab.model.Session;
import com.lab.model.User;
import com.lab.network.ServerConnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 * UserRestaurantsRowController gestisce l'interfaccia relativa alla singola riga della lista di ristoranti presentata all'utente.
 * <p>
 * Permette di visualizzare e gestire la singola riga del ristorante, nascondere le funzionalita' in base alla tipologia di utenza, invocare i giusti metodi per la gestione delle specifiche funzionalita'.
 * </p>
 */
public class UserRestaurantsRowController {
  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text reviewsNum;
  @FXML private Button bookmark;

  private Restaurant restaurant;
  private boolean isBookmarked = false;
  
  /**
   * Imposta il valore dei componenti grafici delle informazioni del ristorante.
   * <p>
   * Dal ristorante ottenuto dalla lista degli argomenti, ne prelega le informazioni e aggiorna la grafica testuale delle relative informazioni.
   * Inoltre si occupa anche dell'icona relativa alla preferenza o meno del ristorante, creando un thread asincrono in background che comunica con il server remoto per ottenere l'informazione relativa alla preferenza da parte dell'utente sullo specifico ristorante. 
   * </p>
   * 
   * @param r Il ristorante da cui reperire i dati aggiuntivi.
   */
  public void setRestaurant(Restaurant r)
  {
    restaurant = r;

    name.setText(r.getName());

    String fullAddress = r.getAddress();
    String shortAddress = fullAddress;
    if(fullAddress.contains(",")){
      String[] split = fullAddress.split(",");
      if(split.length >= 2) shortAddress = split[0] + ", " + split[1]; 
    }
    address.setText(shortAddress);

    starsNum.setText(String.format("%.1f", r.getAverageStars()));
    reviewsNum.setText(String.valueOf(r.getReviewsNum()));
    
    User user = Session.getCurrentUser();
    if(user != null) {
      CompletableFuture.supplyAsync(() -> {
        try {
          return ServerConnection.getServer().isBookmarked(user.getId(), r.getId());
        } catch(RemoteException e) {
          e.printStackTrace();
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dati preferenza ristorante");
          return false;
        }
      }).thenAccept(bookmark -> {
        Platform.runLater(() -> {
          isBookmarked = bookmark;
          updateBookmark();
        });
      });
    } else bookmark.setVisible(false);
  }

  /**
   * Gestisce l'evento di visualizzazione dei dettagli del ristorante da parte di un utente.
   * <p>
   * Chiama il metodo {@link UserHomeController#openDetails(Restaurant)} che si occupa della creazione e caricamento delle informazioni della sovrafinestra dei dettagli.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Dettagli.
   */
  @FXML public void detailClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Detail button clicked");
    UserHomeController.getInstance().openDetails(restaurant);
  }

  /**
   * Gestisce l'evento di aggiunta/rimozione preferenza sul ristorante.
   * <p>
   * In base alla preferenza del ristorante (gia' tra i preferiti o meno), viene creato un thread asincrono in background che comunica con il server per aggiornare le modifiche sulla preferenza del ristorante.
   * Una volta ottenuta la risposta, il thread grafico aggiorna la grafica relativa all'icona di preferenza tramite il metodo {@link #updateBookmark()}.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Preferiti.
   */
  @FXML void bookmarkClicked(ActionEvent event)
  {
    if (Session.getCurrentUser() == null) return;

    int userId = Session.getCurrentUser().getId();
    int restId = restaurant.getId();

    bookmark.setDisable(true);

    if(isBookmarked) {
      CompletableFuture.supplyAsync(() -> {
        try {
          return ServerConnection.getServer().removeBookmark(userId, restId);
        } catch(RemoteException ex) {
          ex.printStackTrace();
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta rimozione preferenza ristorante");
          return false;
        }
      }).thenAccept(success -> {
        Platform.runLater(() -> {
          if(success) isBookmarked = false;
          updateBookmark();
          bookmark.setDisable(false);
          UserHomeController.getInstance().refreshCurrentList();
        });
      });
    } else {
      CompletableFuture.supplyAsync(() -> {
        try {
          return ServerConnection.getServer().addBookmark(userId, restId);
        } catch(RemoteException ex) {
          ex.printStackTrace();
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta aggiunta preferenza ristorante");
          return false;
        }
      }).thenAccept(success -> {
        Platform.runLater(() -> {
          if(success) isBookmarked = true;
          updateBookmark();
          bookmark.setDisable(false);
        });
      });
    }
  }

  /**
   * Aggiorna il valore dell'icona di preferenza del ristorante.
   */
  private void updateBookmark()
  {
    bookmark.getStyleClass().remove("bookmarkButton");
    bookmark.getStyleClass().remove("bookmarkedButton");

    if (isBookmarked) bookmark.getStyleClass().add("bookmarkedButton");
    else bookmark.getStyleClass().add("bookmarkButton");
  }
}
