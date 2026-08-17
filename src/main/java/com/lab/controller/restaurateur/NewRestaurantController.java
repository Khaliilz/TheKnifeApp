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
    initialize();
    boolean error = false;

    String nameR = name.getText().trim();
    String addressR = address.getText().trim();
    String cityR = city.getText().trim();
    String countryR = country.getText().trim();
    String latitudeR = latitude.getText().trim();
    String longitudeR = longitude.getText().trim();
    String cuisineR = cuisine.getText().trim();
    String delivery = ((RadioButton) deliveryGroup.getSelectedToggle()).getText();
    String booking = ((RadioButton) bookingGroup.getSelectedToggle()).getText();
    String price = ((RadioButton) priceGroup.getSelectedToggle()).getText();

    if(nameR.isEmpty()){
      Lib.errorBorder(name);
      error = true;
    }

    String addressRegex = "^.+?,\\s*\\d+[a-zA-Z]?$";
    if(addressR.isEmpty() || !addressR.matches(addressRegex)){
      Lib.errorBorder(address);
      error = true;
    }

    String nameRegex = "^[\\p{L}\\s\\'\\-\\.]+$";
    if(cityR.isEmpty() || !cityR.matches(nameRegex)){
      Lib.errorBorder(city);
      error = true;
    }

    if(countryR.isEmpty() || !countryR.matches(nameRegex)){
      Lib.errorBorder(country);
      error = true;
    }

    String decimalRegex = "^-?\\d+(\\.\\d+)?$";
    if(latitudeR.isEmpty() || !latitudeR.matches(decimalRegex)){
      Lib.errorBorder(latitude);
      error = true;
    }else{
      double lat = Double.parseDouble(latitudeR);
      if(lat<-90.0 || lat>90.0){
        Lib.errorBorder(latitude);
        error = true;
      }
    }

    if(longitudeR.isEmpty() || !longitudeR.matches(decimalRegex)){
      Lib.errorBorder(longitude);
      error = true;
    }else{
      double lon = Double.parseDouble(longitudeR);
      if(lon<-180.0 || lon>180.0){
        Lib.errorBorder(longitude);
        error = true;
      }
    }

    String cuisineRegex = "^[\\p{L}\\s\\'\\-]+(,\\s*[\\p{L}\\s\\'\\-]+)*$";
    if(cuisineR.isEmpty() || !cuisineR.matches(cuisineRegex)){
      Lib.errorBorder(cuisine);
      error = true;
    }

    if(error) return;

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
