package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RightMenuSearchController {
  
  @FXML private TextField luogo_TF;

  @FXML
  public void initialize()
  {
    luogo_TF.setOnAction(this::searchClicked);
    Lib.resetBorder(luogo_TF);
  }

  @FXML
  public void searchClicked(ActionEvent event)
  {
    luogo_TF.getStyleClass().remove("errorInput");
    boolean error = false;
    String place = luogo_TF.getText();
    
    if(place.isEmpty()){
      Lib.errorBorder(luogo_TF);
      error = true;
    }

    if(error) return;

    System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Search: " + place);
    SignedinDefaultController.getInstance().searchByPlace(place);
  }

  @FXML
  public void filterClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Filter");
  }

  @FXML
  public void bookmarkClicked(ActionEvent event)
  {
    SignedinDefaultController.getInstance().loadBookmarked();
    System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Bookmark");
  }

  @FXML
  public void reviewClicked(ActionEvent event)
  {
    SignedinDefaultController.getInstance().loadReviewed();
    System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Review");
  }
}