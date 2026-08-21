package com.lab.controller.user;

import java.io.IOException;

import com.lab.model.Session;
import com.lab.utility.StringColor;
import com.lab.utility.ErrorContainer;

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
    ErrorContainer.resetBorder(luogo_TF);

    if(Session.getCurrentUser() == null) {
      bookmarkedButton.setVisible(false);
      bookmarkedButton.setManaged(false);
      reviewedButton.setVisible(false);
      reviewedButton.setManaged(false);
    }
  }

  @FXML
  public void searchClicked(ActionEvent event)
  {
    boolean error = false;
    String place = luogo_TF.getText();
    
    if(place.isEmpty()) {
      ErrorContainer.errorBorder(luogo_TF);
      error = true;
    }

    if(error) return;

    System.out.println("[" + StringColor.GREEN + "ACTION" + StringColor.RESET + "] Search: " + place);
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
      System.out.println("[" + StringColor.GREEN + "ACTION" + StringColor.RESET + "] Filter button clicked");
    }catch(IOException e) {
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Filter page loading");
      e.printStackTrace();
    }
  }

  @FXML
  public void bookmarkClicked(ActionEvent event)
  {
    UserHomeController.getInstance().loadBookmarked();
    System.out.println("[" + StringColor.GREEN + "ACTION" + StringColor.RESET + "] Bookmark button clicked");
  }

  @FXML
  public void reviewClicked(ActionEvent event)
  {
    UserHomeController.getInstance().loadReviews();
    System.out.println("[" + StringColor.GREEN + "ACTION" + StringColor.RESET + "] Review button clicked");
  }
}