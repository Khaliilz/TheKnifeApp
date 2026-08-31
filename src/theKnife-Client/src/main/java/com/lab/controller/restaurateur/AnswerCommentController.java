/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.restaurateur;

import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * AnswerCommentController Gestisce l'interfaccia relativa al form di risposta ad una recensione per il ristoratore.
 * <p>
 * Instanzia le informazioni relative al nome dell'utente, commento e risposta.
 * Al salvataggio viene mandato una richiesta di aggiunta/aggiornamento al server, che a sua volta la inoltrera' al database.
 * </p>
 */
public class AnswerCommentController {
  
  @FXML private Text name;
  @FXML private TextArea comment;
  @FXML private TextArea answer;
  @FXML private Button saveButton;
  
  private String[] reviewData;

  /**
   * Imposta i valori delle componente grafiche.
   * <p>
   * In base ai dati presente nell'array passato come argomento del metodo, vengono assegnati i vari valori.
   * </p>
   * 
   * @param data valori del nome utente, commento, risposta, id utente e id ristoratore
   */
  public void setReviewData(String[] data)
  {
    reviewData = data;
    
    name.setText(data[0]);

    comment.setText(data[2]);
    comment.setEditable(false);
    comment.setFocusTraversable(false);

    if(data[3] != null) answer.setText(data[3]);
    answer.requestFocus();
    answer.positionCaret(answer.getText().length());
  }

  /**
   * Gestisce l'evento di richiesta di salvataggio della risposta alla recensione al server remoto.
   * <p>
   * L'intefaccia viene temporaneamente disabilitata e viene avviato un thread in background per inviare la richiesta al server remoto.
   * Ricevuta la risposta, il controllo ritorna al thread grafico, e la finestra viene chiusa.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Salva.
   */
  @FXML
  public void saveClicked(ActionEvent event)
  {
    int userId = Integer.parseInt(reviewData[4]);
    int restaurantId = Integer.parseInt(reviewData[5]);
    String answerText = answer.getText().trim();

    saveButton.setDisable(true);
    saveButton.setText("SALVATAGGIO...");

    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().saveReviewAnswer(userId, restaurantId, answerText);
      } catch(RemoteException ex) {
        ex.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta salvataggio dati (risposta recensione)");
        return false;
      }
    }).thenAccept(success -> {
      Platform.runLater(() -> {
        saveButton.setDisable(false);
        saveButton.setText("SALVA");
        if(!success) System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta salvataggio dati (risposta recensione))");
        else {
          System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Risposta alla recensione salvata");
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
          stage.close();
        }
      });
    });
  }
}
