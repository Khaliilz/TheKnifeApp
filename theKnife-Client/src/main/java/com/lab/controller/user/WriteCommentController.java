package com.lab.controller.user;

import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import com.lab.model.Restaurant;
import com.lab.model.Session;
import com.lab.server.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class WriteCommentController {
  @FXML private Text name_R;
  @FXML private TextArea comment;
  @FXML private ToggleGroup starsGroup;
  @FXML private RadioButton starsOne;
  @FXML private RadioButton starsTwo;
  @FXML private RadioButton starsThree;

  private Restaurant currentRestaurant;
  private boolean exists = false;

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

  @FXML void backClicked(ActionEvent event)
  {
    UserHomeController.getInstance().closeWriteComment();
  }

  @FXML
  public void saveClicked(ActionEvent event)
  {
    RadioButton selectedRadioButton = (RadioButton) starsGroup.getSelectedToggle();
    int stars = Integer.parseInt(selectedRadioButton.getText());
    String text = comment.getText();

    int userId = Session.getCurrentUser().getId();
    int restaurantId = currentRestaurant.getId();
    boolean reviewed = false;
    
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
