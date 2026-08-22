package com.lab.controller.restaurateur;

import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import com.lab.server.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AnswerCommentController {
  
  @FXML private Text name;
  @FXML private TextArea comment;
  @FXML private TextArea answer;
  @FXML private Button saveButton;
  
  private String[] reviewData;

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
