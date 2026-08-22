package com.lab.controller.restaurateur;

import com.lab.model.Restaurant;
import com.lab.server.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import java.rmi.RemoteException;

public class YourRestaurantsController {
  
  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text reviewsNum;
  @FXML private Button removeButton;

  private Restaurant currentRestaurant;
  
  public void setRestaurantData(Restaurant r)
  {
    currentRestaurant = r;

    name.setText(r.getName());
    
    String fullAddress = r.getAddress();
    String shortAddress = fullAddress;
    if(fullAddress != null && fullAddress.contains(",")){
      String[] split = fullAddress.split(",");
      if(split.length >= 3) shortAddress = split[0].trim() + ", " + split[1].trim() + ", " + split[2].trim(); 
    }
    address.setText(shortAddress);
    
    starsNum.setText(String.format("%.1f", r.getAverageStars()));
    reviewsNum.setText(String.valueOf(r.getReviewsNum()));
  }

  @FXML public void detailClicked(ActionEvent e)
  {
    RestaurateurHomeController.getInstance().openDetails(currentRestaurant);
  }

  @FXML public void removeClicked(ActionEvent e)
  {
    removeButton.setDisable(true);

    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().removeRestaurant(currentRestaurant.getId());
      } catch(RemoteException ex) {
        ex.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta di rimozione ristorante");
        return false;
      }
    }).thenAccept(success -> {
      Platform.runLater(() -> {
        removeButton.setDisable(false);
        if(!success) System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta di rimozione ristorante");
        else {
          System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Ristorante rimosso");
          RestaurateurHomeController.getInstance().fillRestaurants();
        }
      });
    });
  }
}
