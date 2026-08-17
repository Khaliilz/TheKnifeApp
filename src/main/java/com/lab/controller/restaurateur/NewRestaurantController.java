package com.lab.controller.restaurateur;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
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

  @FXML
  public void initialize()
  {
    Lib.resetBorder(name);
    Lib.resetBorder(address);
    Lib.resetBorder(city);
    Lib.resetBorder(country);
    Lib.resetBorder(latitude);
    Lib.resetBorder(longitude);
    Lib.resetBorder(cuisine);
  }

  @FXML
  public void saveClicked(ActionEvent e)
  {
    String nameR = name.getText();
    String addressR = address.getText();
    String cityR = city.getText();
    String countryR = country.getText();
    String latitudeR = latitude.getText();
    String longitudeR = longitude.getText();
    String cuisineR = cuisine.getText();
    RadioButton deliverySelected = (RadioButton) deliveryGroup.getSelectedToggle();
    String delivery = deliverySelected.getText();
    RadioButton bookingSelected = (RadioButton) deliveryGroup.getSelectedToggle();
    String booking = bookingSelected.getText();
    RadioButton priceSelected = (RadioButton) deliveryGroup.getSelectedToggle();
    String price = priceSelected.getText();

    if(nameR.isEmpty()){
      Lib.errorBorder(name);
      return;
    }

    if(addressR.isEmpty()){
      Lib.errorBorder(address);
      return;
    }

    if(cityR.isEmpty()){
      Lib.errorBorder(city);
      return;
    }

    if(countryR.isEmpty()){
      Lib.errorBorder(country);
      return;
    }

    if(latitudeR.isEmpty()){
      Lib.errorBorder(latitude);
      return;
    }

    if(longitudeR.isEmpty()){
      Lib.errorBorder(longitude);
      return;
    }

    if(cuisineR.isEmpty()){
      Lib.errorBorder(cuisine);
      return;
    }

    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Save button clicked");
    RestaurateurHomeController.getInstance().closeNewRestaurant();
  }

  @FXML
  public void cancelClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Cancel button clicked");
    RestaurateurHomeController.getInstance().closeNewRestaurant();
  }

}
