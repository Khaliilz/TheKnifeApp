package com.lab.controller.restaurateur;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import com.lab.App;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.database.model.Restaurant;
import com.lab.server.ServerConnection;
import com.lab.utility.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DetailsRestaurateurController {

  @FXML private VBox list;
  @FXML private ScrollPane listContainer;
  @FXML private Label emptyLabel;

  private Restaurant currentRestaurant;

  @FXML
  public void initialize()
  {
    PageController.showTitle(false); 
    ToolbarController.showBackButton(false);
    ToolbarController.showLeftSide(false, false, true);
  }

  public void setRestaurant(Restaurant r)
  {
    currentRestaurant = r;
    fillReviews();
  }

  public void fillReviews()
  {
    list.getChildren().clear();
    if(currentRestaurant == null) return;

    try{
      List<String[]> reviews = ServerConnection.getServer().getRestaurateurReviews(currentRestaurant.getId());
      
      boolean isEmpty = reviews.isEmpty();
      emptyLabel.setVisible(isEmpty);
      emptyLabel.setManaged(isEmpty);
      listContainer.setVisible(!isEmpty);
      if(isEmpty) return;

      for(String[] r : reviews) {
        try{
          FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/restaurateur/yourReviews.fxml"));
          HBox row = loader.load();

          YourReviewsController controller = loader.getController();
          String[] fullData = {r[0], r[1], r[2], r[3] != null ? r[3] : "", r[4], String.valueOf(currentRestaurant.getId())};
          controller.setReviewData(fullData);

          list.getChildren().add(row);
        }catch(IOException e) {
          System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Filling your reviews list");
          e.printStackTrace();
        }
      }
    } catch (RemoteException e) {
      e.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
  }

  @FXML public void backClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Back button clicked");
    RestaurateurHomeController.getInstance().closeDetails();
  }
}
