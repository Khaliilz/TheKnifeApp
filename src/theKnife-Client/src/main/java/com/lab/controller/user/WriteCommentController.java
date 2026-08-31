/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.user;

import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import com.lab.model.Restaurant;
import com.lab.model.Session;
import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

/**
 * WriteCommentController gestisce l'interfaccia relativa alla scrittura di una recensione.
 */
public class WriteCommentController {
  @FXML private Text name_R;
  @FXML private TextArea comment;
  @FXML private ToggleGroup starsGroup;
  @FXML private RadioButton starsOne;
  @FXML private RadioButton starsTwo;
  @FXML private RadioButton starsThree;

  private Restaurant currentRestaurant;
  private boolean exists = false;

  /**
   * Imposta il valore dei componenti grafici delle informazioni relative alla recensione dell'utente.
   * <p>
   * Selezione come titolo il nome del ristorante da recensire.
   * Controlla se l'utente ha recensito precedentemente questo ristorante.
   * Per il controllo viene eseguito un thread asincrono in background per la richiesta al server remoto delle informazioni.
   * Se lo ha gia' recensito, carica la recensione precedente, in modo da permettere all'utente di modificarla.
   * </p>
   * 
   * @param restaurant Il ristorante da cui reperire i dati aggiuntivi.
   */
  public void setRestaurantReview(Restaurant restaurant)
  {
    currentRestaurant = restaurant;
    name_R.setText(restaurant.getName());

    int userId = Session.getCurrentUser().getId();
    int restaurantId = currentRestaurant.getId();

    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().getUserReview(userId, restaurantId);
      } catch(RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta testo recensione esistente");
        return null;
      }
    }).thenAccept(review -> {
      Platform.runLater(() -> {
        if(review != null) {
          exists = true;
          comment.setText(review[1] != null ? review[1] : "");
          int userStar = Integer.parseInt(review[0]);
          if(userStar == 1) starsOne.setSelected(true);
          else if(userStar == 2) starsTwo.setSelected(true);
          else starsThree.setSelected(true);
        }
      });
    });
  }

  /**
   * Gestisce l'evento di ritorno indietro dalla scrittura/modifica di una recensione.
   * <p>
   * Chiama il metodo {@link UserHomeController#closeWriteComment()} che si occupa della chiusura della sovrafinestra della scrittura di una recensione.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Indietro.
   */
  @FXML void backClicked(ActionEvent event)
  {
    UserHomeController.getInstance().closeWriteComment();
  }

  /**
   * Gestisce l'evento di ritorno salvataggio relativo alla scrittura della recensione o al suo aggiornamento.
   * <p>
   * Se la recensione e' nuova, viene eseguito una richiesta di inserimento della recensione, altrimenti viene eseguita una richiesta di aggiornamento.
   * Viene messo temporanemanete in pausa il thread grafico, creato e lanciato un thread asincrono in background per la comunicazione con il server remoto.
   * Una volta ottenuta la risposta viene chiusa la finestra di dialogo tramite {@link UserHomeController#closeWriteComment()}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Indietro.
   */
  @FXML
  public void saveClicked(ActionEvent event)
  {
    RadioButton selectedRadioButton = (RadioButton) starsGroup.getSelectedToggle();
    int stars = Integer.parseInt(selectedRadioButton.getText());
    String text = comment.getText();

    int userId = Session.getCurrentUser().getId();
    int restaurantId = currentRestaurant.getId();
    //boolean reviewed = false;
    
    if(exists) {
      CompletableFuture.supplyAsync(() -> {
        try {
          return ServerConnection.getServer().updateReview(userId, restaurantId, stars, text);
        } catch(RemoteException ex) {
          ex.printStackTrace();
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta aggiornamento recensione");
          return false;
        }
      }).thenAccept(review -> {
        Platform.runLater(() -> {
          if(!review) System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta aggiornamento recensione");
          else {
            System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Recensione aggiornata");
            UserHomeController.getInstance().closeWriteComment();
          }
        });
      });
    } else {
      CompletableFuture.supplyAsync(() -> {
        try {
          return ServerConnection.getServer().addReview(userId, restaurantId, stars, text);
        } catch(RemoteException ex) {
          ex.printStackTrace();
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta salvataggio recensione");
          return false;
        }
      }).thenAccept(review -> {
        Platform.runLater(() -> {
          if(!review) System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta salvataggio recensione");
          else {
            System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Recensione salvata");
            UserHomeController.getInstance().closeWriteComment();
          }
        });
      });
    }
  }
}
