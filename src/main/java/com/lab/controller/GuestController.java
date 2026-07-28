package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class GuestController {
  
  @FXML private TextField luogo_TF;
  @FXML private Button cerca_B;

  @FXML
  public void initialize()
  {
    PageController.showTitle(true);
    ToolbarController.showBackButton(true);
    luogo_TF.setOnAction(this::cercaClicked);
    Lib.resetBorder(luogo_TF);
  }

  @FXML
  public void cercaClicked(ActionEvent event)
  {
    String luogo = luogo_TF.getText().trim();
    if(luogo.isEmpty()){
      Lib.errorBorder(luogo_TF);
      return;
    }
    System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Guest serching [" + luogo + "]");
  }
}
