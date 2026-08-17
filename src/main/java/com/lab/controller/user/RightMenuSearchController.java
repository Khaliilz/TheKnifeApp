package com.lab.controller.user;

import java.io.IOException;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RightMenuSearchController {
  
  @FXML private TextField luogo_TF;
  @FXML private Button bookmarkedButton;
  @FXML private Button reviewedButton;

  @FXML
  public void initialize()
  {
    luogo_TF.setOnAction(this::searchClicked);
    Lib.resetBorder(luogo_TF);

    if(UserHomeController.isGuest){
      bookmarkedButton.setVisible(false);
      bookmarkedButton.setManaged(false);
      reviewedButton.setVisible(false);
      reviewedButton.setManaged(false);
    }
  }

  @FXML
  public void searchClicked(ActionEvent event)
  {
    initialize();
    boolean error = false;
    String place = luogo_TF.getText();
    
    if(place.isEmpty()){
      Lib.errorBorder(luogo_TF);
      error = true;
    }

    if(error) return;

    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Search: " + place);
    UserHomeController.getInstance().searchByPlace(place);
  }

  @FXML
  public void filterClicked(ActionEvent event)
  {
    try{
      Parent filterRoot = FXMLLoader.load(getClass().getResource("/com/lab/fxml/user/filter.fxml"));
      
      Stage popupStage = new Stage();
      
      popupStage.setTitle("TheKnife - Filter");
      Image icon = new Image(getClass().getResource("/com/lab/img/logo.png").toExternalForm());
		  popupStage.getIcons().add(icon);
      popupStage.initStyle(StageStyle.TRANSPARENT); 
      popupStage.setResizable(false);
      popupStage.initModality(Modality.APPLICATION_MODAL); 
      
      Scene scene = new Scene(filterRoot, 450, 530);
      scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
      popupStage.setScene(scene);
      popupStage.show();
      System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Filter button clicked");
    }catch(IOException e){
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Filter page loading");
      e.printStackTrace();
    }
  }

  @FXML
  public void bookmarkClicked(ActionEvent event)
  {
    UserHomeController.getInstance().loadBookmarked();
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Bookmark button clicked");
  }

  @FXML
  public void reviewClicked(ActionEvent event)
  {
    UserHomeController.getInstance().loadReviews();
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Review button clicked");
  }
}