package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class YourRestaurantsController {
  
  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text reviewsNum;

  private String restaurantName;
  
  public void setRestaurantData(String[] r)
  {
    restaurantName = r[0];
    name.setText(r[0]);
    address.setText(r[1]);
    starsNum.setText(r[2]);
    reviewsNum.setText(r[3]);
  }

  @FXML public void detailClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Detail button pressed");
    RestaurateurHomeController.getInstance().openDetails(restaurantName);
  }
}
