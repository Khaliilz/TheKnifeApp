package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class FilterController {
  @FXML
  public void applyFilterClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Filter scene closed");

    UserHomeController.getInstance().applyFilters();

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
  }
}
