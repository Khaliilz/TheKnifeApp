package com.lab.controller;

import java.io.IOException;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    try{
      Parent filterRoot = FXMLLoader.load(getClass().getResource("/com/lab/filter.fxml"));
      
      Stage filterStage = new Stage();
      
      filterStage.setTitle("TheKnife - Filter");
      Image icon = new Image(getClass().getResource("/com/lab/img/logo.png").toExternalForm());
		  filterStage.getIcons().add(icon);
      filterStage.initStyle(StageStyle.TRANSPARENT); 
      filterStage.setResizable(false);
      filterStage.initModality(Modality.APPLICATION_MODAL); 
      
      Scene scene = new Scene(filterRoot, 450, 550);
      scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
      filterStage.setScene(scene);
      filterStage.show();
      System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Filter pressed");
    }catch(IOException e){
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Filter page loading");
      e.printStackTrace();
    }
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