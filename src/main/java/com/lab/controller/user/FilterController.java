package com.lab.controller.user;

import com.lab.utility.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class FilterController {
  
  @FXML private TextField cuisine_TF;
  @FXML private ToggleGroup priceGroup;
  @FXML private ToggleGroup deliveryGroup;
  @FXML private ToggleGroup starsGroup;
  @FXML private ToggleGroup bookingGroup;

  @FXML
  public void applyFilterClicked(ActionEvent event)
  {
    String cuisine = cuisine_TF.getText();
    if(cuisine != null && cuisine.trim().isEmpty()) cuisine = null;

    String price = priceGroup.getSelectedToggle() != null ? ((RadioButton) priceGroup.getSelectedToggle()).getText() : null;
    String delivery = deliveryGroup.getSelectedToggle() != null ? ((RadioButton) deliveryGroup.getSelectedToggle()).getText() : null;
    String stars = starsGroup.getSelectedToggle() != null ? ((RadioButton) starsGroup.getSelectedToggle()).getText() : null;
    String booking = bookingGroup.getSelectedToggle() != null ? ((RadioButton) bookingGroup.getSelectedToggle()).getText() : null;

    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Filter scene closed");

    UserHomeController.getInstance().applyFilters(cuisine, price, delivery, booking, stars);

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
  }
}
