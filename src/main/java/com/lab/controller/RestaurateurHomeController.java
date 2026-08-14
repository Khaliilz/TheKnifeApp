package com.lab.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.lab.App;
import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RestaurateurHomeController {
  
  @FXML private Text title;
  @FXML private VBox list;
  @FXML private StackPane contentArea;
  @FXML private VBox mainArea;
  private javafx.scene.Node detailsNode;

  private static RestaurateurHomeController instance;

  @FXML
  public void initialize()
  {
    instance = this;

    PageController.showTitle(false); 
    ToolbarController.showBackButton(false);

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
    String[] restaurant = {"Ristorante", "Via Roma, 12, Lazio", "5", "10"};
    ArrayList<String[]> lists = new ArrayList<>();
    for(int i=0; i<10; i++) lists.add(restaurant);

    for(String[] r : lists){
      try{
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/yourRestaurants.fxml"));
        HBox row = loader.load();

        YourRestaurantsController controller = loader.getController();
        controller.setRestaurantData(r);
        
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");

        list.getChildren().addAll(row, separator);
      }catch(IOException e){
        System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Filling your restaurants list");
        e.printStackTrace();
      }
    }
  }

  public void openDetails(String r)
  {
    try{
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/detailsRestaurateur.fxml"));
      detailsNode = loader.load();

      setTitle(r);

      mainArea.setVisible(false);
      contentArea.getChildren().add(detailsNode);

      System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Restaurant Reviews opended");
    }catch(IOException e){
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading details view");
      e.printStackTrace();
    }
  }

  public void closeDetails()
  {
    if(detailsNode != null){
      contentArea.getChildren().remove(detailsNode);
      detailsNode = null;
    }

    setTitle("I tuoi ristoranti");
    mainArea.setVisible(true);
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Your restaurants displayed");
  }

  @FXML public void addClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Add button clicked");
  }
}
