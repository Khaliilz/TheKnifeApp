package com.lab.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.lab.App;
import com.lab.Lib;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserHomeController {
  
  @FXML private StackPane rightMenuArea;
  @FXML private Text title;
  @FXML private VBox listOfRestaurants;
  @FXML private ScrollPane listContainer;
  @FXML private StackPane leftMenuArea;
  @FXML private Button bookmark;

  private static UserHomeController instance;
  private javafx.scene.Node detailsNode;
  private javafx.scene.Node commentNode;

  public static Boolean isGuest = false;

  @FXML
  public void initialize()
  {
    instance = this;

    PageController.showTitle(false); 
    ToolbarController.showBackButton(false);
    loadRightMenu("Ristoranti nelle vicinanze", "rightMenuSearch.fxml");
    
    loadNearest();
  }

  public static UserHomeController getInstance()
  {
    return instance;
  }

  public void loadNearest()
  {
    title.setText("Ristoranti nelle vicinanze");
    listOfRestaurants.getChildren().clear();

    fillRestaurants();
  }

  public void loadBookmarked()
  {
    closeDetails();
    closeComment();

    title.setText("Ristoranti preferiti");
    listOfRestaurants.getChildren().clear();

    fillRestaurants();
  }

  public void loadReviews()
  {
    closeDetails();
    closeComment();

    title.setText("Ristoranti recensiti");
    listOfRestaurants.getChildren().clear();

    fillReviewed();
  }

  public void loadRightMenu(String newTitle, String fileName)
  {
    try{
      title.setText(newTitle);
      Parent selectedMenu = FXMLLoader.load(App.class.getResource(fileName));
      rightMenuArea.getChildren().setAll(selectedMenu);
    }catch(IOException e) {
      System.out.print("[" + Lib.RED + "ERROR" + Lib.RESET + "] fail to load the right menu: " + fileName);
      e.printStackTrace();
    }
  }

  public void searchByPlace(String place)
  {
    closeDetails();
    closeComment();

    title.setText("Ristoranti a " + place);
    listOfRestaurants.getChildren().clear();

    fillRestaurants();
  }

  public void applyFilters()
  {
    closeDetails();
    closeComment();
    
    title.setText("Ristoranti trovati");
    listOfRestaurants.getChildren().clear();
    
    fillRestaurants(); 
  }

  private void fillRestaurants() 
  {
    String[] ristorante1 = {"Ristorante 1", "via Trieste 12, Milano", "5", "10"};
    ArrayList<String[]> ristoranti = new ArrayList<>();
    for(int i=0; i<15; i++) ristoranti.add(ristorante1);

    listOfRestaurants.getChildren().clear();

    for(String[] r : ristoranti){
      try {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/userRestaurantsRow.fxml"));
        HBox row = loader.load();

        UserRestaurantsRowController controller = loader.getController();
        controller.setRestaurantData(r);

        listOfRestaurants.getChildren().add(row);
       }catch (IOException e){
        System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading user restaurants");
        e.printStackTrace();
       }
    }
  }

  private void fillReviewed()
  {
    String[] ristorante1 = {"Ristorante 1", "via Trieste 12, Milano", "5", "10"};
    ArrayList<String[]> ristoranti = new ArrayList<>();
    for(int i=0; i<15; i++) ristoranti.add(ristorante1);

    listOfRestaurants.getChildren().clear();

    for(String[] r : ristoranti){
      String[] comment = {"Ristorante 1", "Ottimo!", "Grazie!", "3"};

      try {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/restaurantReviewsRow.fxml"));
        HBox row = loader.load();

        RestaurantReviewsRowController controller = loader.getController();
        controller.setReview(r, comment);

        listOfRestaurants.getChildren().add(row);
       }catch (IOException e){
        System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading reviewed restaurants");
        e.printStackTrace();
       }
    }
  }

  public void openDetails(String[] restaurant)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/detailsUser.fxml"));
      detailsNode = loader.load();

      DetailsUserController controller = loader.getController();
      controller.setDetails(restaurant);

      listContainer.setVisible(false); 
      leftMenuArea.getChildren().add(detailsNode); 

    }catch(IOException e) {
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading details");
      e.printStackTrace();
    }
  }

  public void closeDetails()
  {
    if(detailsNode != null){
      leftMenuArea.getChildren().remove(detailsNode);
      detailsNode = null;
    }
    
    listContainer.setVisible(true); 
  }

  public void viewComment(String[] comment)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/viewComment.fxml"));
      commentNode = loader.load();

      ViewCommentController controller = loader.getController();
      controller.setComment(comment);

      listContainer.setVisible(false); 
      leftMenuArea.getChildren().add(commentNode); 

    }catch(IOException e) {
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading comment");
      e.printStackTrace();
    }
  }

  public void closeComment()
  {
    if(commentNode != null){
      leftMenuArea.getChildren().remove(commentNode);
      commentNode = null;
    }
    
    listContainer.setVisible(true); 
  }

  public void openWriteComment(String restaurantName)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/writeComment.fxml"));
      commentNode = loader.load();

      WriteCommentController controller = loader.getController();
      controller.setRestaurantName(restaurantName);

      if (detailsNode != null) detailsNode.setVisible(false);
      leftMenuArea.getChildren().add(commentNode); 

    }catch(IOException e){
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading write comment page");
      e.printStackTrace();
    }
  }

  public void closeWriteComment()
  {
    if(commentNode != null){
      leftMenuArea.getChildren().remove(commentNode);
      commentNode = null;
    }
    
    if (detailsNode != null) detailsNode.setVisible(true);
  }
}