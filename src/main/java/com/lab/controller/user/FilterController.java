package com.lab.controller.user;

import com.lab.utility.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class FilterController {
  
  @FXML private ToggleGroup priceGroup;
  @FXML private ToggleGroup deliveryGroup;
  @FXML private ToggleGroup starsGroup;
  @FXML private ToggleGroup bookingGroup;

  @FXML
  public void applyFilterClicked(ActionEvent event)
  {
    RadioButton price = (RadioButton) priceGroup.getSelectedToggle();
    RadioButton delivery = (RadioButton) deliveryGroup.getSelectedToggle();
    RadioButton stars = (RadioButton) starsGroup.getSelectedToggle();
    RadioButton booking = (RadioButton) bookingGroup.getSelectedToggle();

    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Filter scene closed");

    UserHomeController.getInstance().applyFilters();

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
  }
}
