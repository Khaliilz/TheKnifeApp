/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.user;

import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import com.lab.model.Session;
import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * ViewCommentController gestisce l'interfaccia relativa alla visualizzazione di una recensione dalla lista recensiti.
 * <p>
 * Permette di visualizzare una propria recensione nel dettagli, di modificarla e se richiesto anche di rimuoverla.
 * </p> 
 */
public class ViewCommentController {

  @FXML private Text name_R;
  @FXML private TextArea comment;
  @FXML private Text answer;
  @FXML private RadioButton starsOne;
  @FXML private RadioButton starsTwo;
  @FXML private RadioButton starsThree;
  @FXML private Button saveButton;
  @FXML private Button removeButton;

  private int currentRestaurantId;
  private int prevStars;
  private String prevComment;

  /**
   * Imposta il valore dei componenti grafici delle informazioni della recensione.
   * <p>
   * Dalla recensione ottenuta dalla lista degli argomenti, ne prelega le informazioni e aggiorna la grafica testuale delle relative informazioni.
   * </p>
   * 
   * @param c La recensione da cui reperire i dati aggiuntivi.
   */
  public void setComment(String[] c)
  {
    name_R.setText(c[0]);
    comment.setText(c[1]);
    answer.setText(c[2]);
    
    currentRestaurantId = Integer.parseInt(c[4]);

    int stars = Integer.parseInt(c[3]);
    switch(stars) {
      case 1:
        starsOne.setSelected(true);
        break;
      case 2:
        starsTwo.setSelected(true);
        break;
      case 3:
        starsThree.setSelected(true);
        break;
      default:
        starsOne.setSelected(true);
    }

    prevStars = stars;
    prevComment = c[1];
  }

  /**
   * Gestisce l'evento di salvataggio delle modifiche applicate ad una propria recensione.
   * <p>
   * Controlla se sono state fatte modifiche o meno.
   * Se non sono state fatte non viene creato il thread asincrono e comunicato con il server remoto, e viene eseguita direttamente la chiusura della finestra tramite {@link UserHomeController#closeComment()}
   * Altrimenti viene temporaneamente ceduto il controllo da parte del thread grafico a un thread asincrono per la comunicazione con il server.
   * Una volta ottenuta la risposta viene chiusa la finestra di dialogo tramite {@link UserHomeController#closeComment()}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Indietro.
   */
  @FXML
  public void saveClicked(ActionEvent event)
  {
    int stars = 1;
    if(starsTwo.isSelected()) stars = 2;
    else if(starsThree.isSelected()) stars = 3;
    int tmpStars = stars;

    if(prevStars == tmpStars && prevComment.equals(comment.getText())) {
      UserHomeController.getInstance().closeComment();
      return;
    }
    saveButton.setDisable(true);
    saveButton.setText("SALVATAGGIO...");

    int userId = Session.getCurrentUser().getId();
    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().updateReview(userId, currentRestaurantId, tmpStars, comment.getText());
      } catch(RemoteException ex) {
        ex.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta salvataggio recensione");
        return false;
      }
    }).thenAccept(success -> {
      Platform.runLater(() -> {
        saveButton.setDisable(false);
        saveButton.setText("SALVA");
        if(!success) System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta salvataggio recensione");
        else {
          System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Recensione salvata");
          UserHomeController.getInstance().closeComment();
        }
      });
    });
  }

  /**
   * Gestisce l'evento di rimozione di una propria recensione.
   * <p>
   * Viene temporaneamente ceduto il controllo da parte del thread grafico a un thread asincrono per la comunicazione con il server.
   * Una volta ottenuta la risposta viene chiusa la finestra di dialogo tramite {@link UserHomeController#closeComment()}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Rimuovi.
   */
  @FXML void removeClicked(ActionEvent event)
  { 
    removeButton.setDisable(true);
    int userId = Session.getCurrentUser().getId();

    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().removeReview(userId, currentRestaurantId);
      } catch(RemoteException ex) {
        ex.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta rimozione recensione");
        return false;
      }
    }).thenAccept(success -> {
      Platform.runLater(() -> {
        removeButton.setDisable(false);
        removeButton.setText("RIMUOVI");
        if(!success) System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta rimozione recensione");
        else {
          System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Recensione rimossa");
          UserHomeController.getInstance().closeComment();
        }
      });
    });
  }
}
