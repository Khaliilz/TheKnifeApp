package com.lab.controller.user;

import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import com.lab.model.Session;
import com.lab.server.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

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

  @FXML
  public void saveClicked(ActionEvent e)
  {
    int stars = 1;
    if(starsTwo.isSelected()) stars = 2;
    else if(starsThree.isSelected()) stars = 3;

    saveButton.setDisable(true);
    saveButton.setText("SALVATAGGIO...");

    int userId = Session.getCurrentUser().getId();
    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().updateReview(userId, currentRestaurantId, stars, comment.getText());
      } catch(RemoteException ex) {
        e.printStackTrace();
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
          UserHomeController.getInstance().loadReviews();
        }
      });
    });
  }

  @FXML void removeClicked(ActionEvent e)
  { 
    removeButton.setDisable(true);
    int userId = Session.getCurrentUser().getId();

    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().removeReview(userId, currentRestaurantId);
      } catch(RemoteException ex) {
        e.printStackTrace();
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
          UserHomeController.getInstance().loadReviews();
        }
      });
    });
  }
  
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
  }
}
