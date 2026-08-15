package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class NewRestaurantController {
  
  @FXML private TextField name;
  @FXML private TextField address;
  @FXML private TextField city;
  @FXML private TextField country;
  @FXML private TextField latitude;
  @FXML private TextField longitude;
  @FXML private TextField cuisine;
  @FXML private ToggleGroup deliveryGroup;
  @FXML private ToggleGroup bookingGroup;
  @FXML private ToggleGroup priceGroup;


  @FXML public void saveClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Save button clicked [" + name.getText() + "]");
    RestaurateurHomeController.getInstance().closeNewRestaurant();
  }

  @FXML public void cancelClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Cancel button clicked");
    RestaurateurHomeController.getInstance().closeNewRestaurant();
  }

}
