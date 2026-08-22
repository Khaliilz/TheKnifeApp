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

public class UserRestaurantsRowController {
  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text reviewsNum;
  @FXML private Button bookmark;

  private Restaurant restaurant;
  private boolean isBookmarked = false;
  
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

  @FXML public void detailClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Detail button clicked");
    UserHomeController.getInstance().openDetails(restaurant);
  }

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
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dati preferenza ristorante");
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
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dati preferenza ristorante");
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

  private void updateBookmark()
  {
    bookmark.getStyleClass().remove("bookmarkButton");
    bookmark.getStyleClass().remove("bookmarkedButton");

    if (isBookmarked) bookmark.getStyleClass().add("bookmarkedButton");
    else bookmark.getStyleClass().add("bookmarkButton");
  }
}
