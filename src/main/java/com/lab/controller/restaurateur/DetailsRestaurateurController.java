package com.lab.controller.restaurateur;

import java.io.IOException;
import java.util.ArrayList;

import com.lab.App;
import com.lab.Lib;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DetailsRestaurateurController {

  @FXML private VBox list;

  @FXML
  public void initialize()
  {
    PageController.showTitle(false); 
    ToolbarController.showBackButton(false);
    ToolbarController.showSigninButton(true);
    ToolbarController.showSignupButton(true);
    ToolbarController.showSignoutButton(false);
    
    fillReviews();
  }

  public void fillReviews()
  {
    String[] review = {"Carlo", "Ottimo!", "5", "Grazie!"};
    ArrayList<String[]> lists = new ArrayList<>();
    for(int i=0; i<10; i++) lists.add(review);

    for(String[] r : lists){
      try{
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/restaurateur/yourReviews.fxml"));
        HBox row = loader.load();

        YourReviewsController controller = loader.getController();
        controller.setReviewData(r);
        
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");

        list.getChildren().addAll(row, separator);
      }catch(IOException e){
        System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Filling your reviews list");
        e.printStackTrace();
      }
    }
  }
  @FXML public void backClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Back button clicked");
    RestaurateurHomeController.getInstance().closeDetails();
  }
}
