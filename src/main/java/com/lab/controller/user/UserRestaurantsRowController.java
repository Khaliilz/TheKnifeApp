package com.lab.controller.user;

import com.lab.utility.Lib;

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

  private String[] restaurantData;
  private boolean isBookmarked;
  
  public void setRestaurantData(String[] r)
  {
    if(UserHomeController.isGuest){
      bookmark.setVisible(false);
      bookmark.setManaged(false);
    }
    restaurantData = r;
    name.setText(r[0]);
    address.setText(r[1]);
    starsNum.setText(r[2]);
    reviewsNum.setText(r[3]);
  }

  @FXML public void detailClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Detail button clicked");
    UserHomeController.getInstance().openDetails(restaurantData);
  }

  @FXML void bookmarkClicked(ActionEvent e)
  { 
    if(isBookmarked){
      bookmark.getStyleClass().add("bookmarkButton");
      System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Bookmarked [" + restaurantData[0] + "]");
    } else {
      bookmark.getStyleClass().add("bookmarkedButton");
      System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Unbookmarked [" + restaurantData[0] + "]");
    }
  }
}
