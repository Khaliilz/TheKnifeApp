package com.lab.controller.restaurateur;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import com.lab.App;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.model.Session;
import com.lab.server.ServerConnection;
import com.lab.model.Restaurant;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RestaurateurHomeController {
  
  @FXML private Text title;
  @FXML private VBox list;
  @FXML private StackPane contentArea;
  @FXML private ScrollPane listContainer;
  @FXML private Label emptyLabel;
  @FXML private VBox mainArea;

  private javafx.scene.Node detailsNode;
  private javafx.scene.Node newRestaurantNode;

  private static RestaurateurHomeController instance;

  @FXML
  public void initialize()
  {
    instance = this;

    PageController.showTitle(false);
    ToolbarController.showBackButton(false);
    ToolbarController.showLeftSide(false, false, true);

    title.setText("I tuoi ristoranti");
    fillRestaurants();
  }

  public static RestaurateurHomeController getInstance()
  {
    return instance;
  }

  public void setTitle(String t)
  {
    title.setText(t);
  }

  public void fillRestaurants()
  {
    list.getChildren().clear();

    if (Session.getCurrentUser() == null) return;
    int ownerId = Session.getCurrentUser().getId();

    try{
      List<Restaurant> restaurants = ServerConnection.getServer().getRestaurantsByOwner(ownerId);
      
      boolean isEmpty = restaurants.isEmpty();
      emptyLabel.setVisible(isEmpty);
      emptyLabel.setManaged(isEmpty);
      listContainer.setVisible(!isEmpty);
      if(isEmpty) return;

      for(Restaurant r : restaurants) {
        try{
          FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/restaurateur/yourRestaurants.fxml"));
          HBox row = loader.load();

          YourRestaurantsController controller = loader.getController();
          controller.setRestaurantData(r);

          list.getChildren().add(row);
        }catch(IOException e) {
          System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Filling your restaurants list");
          e.printStackTrace();
        }
      }
    } catch (RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Server comunication");
    }
  }

  public void openDetails(Restaurant r)
  {
    try{
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/restaurateur/detailsRestaurateur.fxml"));
      detailsNode = loader.load();

      setTitle(r.getName());
      
      DetailsRestaurateurController controller = loader.getController();
      controller.setRestaurant(r);

      mainArea.setVisible(false);
      contentArea.getChildren().add(detailsNode);

      System.out.println("[" + StringColor.GREEN + "ACTION" + StringColor.RESET + "] Restaurant Reviews opended");
    }catch(IOException e) {
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Loading details view");
      e.printStackTrace();
    }
  }

  public void closeDetails()
  {
    if(detailsNode != null) {
      contentArea.getChildren().remove(detailsNode);
      detailsNode = null;
    }

    setTitle("I tuoi ristoranti");
    mainArea.setVisible(true);
    System.out.println("[" + StringColor.GREEN + "ACTION" + StringColor.RESET + "] Your restaurants displayed");
  }

  @FXML public void addClicked(ActionEvent e)
  {
    System.out.println("[" + StringColor.GREEN + "ACTION" + StringColor.RESET + "] Add button clicked");
    openNewRestaurant();
  }

  private void openNewRestaurant()
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/restaurateur/newRestaurant.fxml"));
      newRestaurantNode = loader.load();

      setTitle("Nuovo Ristorante");

      mainArea.setVisible(false);
      contentArea.getChildren().add(newRestaurantNode);
    } catch(IOException ex) {
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Loading new restaurant view");
      ex.printStackTrace();
    }
  }

  public void closeNewRestaurant()
  {
    if(newRestaurantNode != null) {
      contentArea.getChildren().remove(newRestaurantNode);
      newRestaurantNode = null;
    }

    setTitle("I tuoi ristoranti");
    mainArea.setVisible(true);
  }
}
