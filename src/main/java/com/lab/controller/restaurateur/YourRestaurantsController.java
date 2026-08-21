package com.lab.controller.restaurateur;

import com.lab.database.model.Restaurant;
import com.lab.database.query.RestaurantQ;
import com.lab.utility.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class YourRestaurantsController {
  
  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text reviewsNum;

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
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Detail button clicked");
    RestaurateurHomeController.getInstance().openDetails(currentRestaurant);
  }

  @FXML public void removeClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Remove button clicked");
    if(currentRestaurant != null) {
      boolean success = RestaurantQ.removeRestaurant(currentRestaurant.getId());
        
      if(success) {
        System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Restaurant removed successfully!");
        RestaurateurHomeController.getInstance().fillRestaurants();
      } else System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Failed to remove restaurant.");
    }
  }
}
