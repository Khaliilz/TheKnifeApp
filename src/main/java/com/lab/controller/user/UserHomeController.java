package com.lab.controller.user;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import javafx.event.ActionEvent;

import com.lab.App;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.database.model.Restaurant;
import com.lab.database.model.Session;
import com.lab.database.model.User;
import com.lab.server.ServerConnection;
import com.lab.utility.Lib;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
  @FXML private Button loadMoreButton;
  @FXML private Label emptyLabel;

  private static UserHomeController instance;
  private javafx.scene.Node detailsNode;
  private javafx.scene.Node commentNode;

  private String currentSearchPlace = "";
  private String filterCuisine;
  private String filterPrice;
  private String filterDelivery;
  private String filterBooking;
  private String filterStars;
  private int currentSearchOffset = 0;

  public static String guestSearchPlace = null;

  @FXML
  public void initialize()
  {
    instance = this;
    
    PageController.showTitle(false);
    ToolbarController.showBackButton(false);
    if(Session.getCurrentUser() == null) {
      ToolbarController.showLeftSide(true, true, false);
    } else {
      ToolbarController.showLeftSide(false, false, true);
    }
    
    loadRightMenu("Ristoranti nelle vicinanze", "/com/lab/fxml/user/rightMenuSearch.fxml");
    
    if(guestSearchPlace != null && !guestSearchPlace.isEmpty()) {
      String placeToSearch = guestSearchPlace;
      guestSearchPlace = null;
      searchByPlace(placeToSearch);
    } else loadNearest();
  }

  public static UserHomeController getInstance()
  {
    return instance;
  }

  public void loadNearest()
  {
    if(loadMoreButton != null) {
      loadMoreButton.setVisible(false);
      loadMoreButton.setManaged(false);
    }
    title.setText("Ristoranti nelle vicinanze");
    listOfRestaurants.getChildren().clear();
    
    User user = Session.getCurrentUser();

    try{
      double lat = user.getLatitude();
      double lon = user.getLongitude();

      List<Restaurant> nearest = ServerConnection.getServer().getNearestRestaurants(lat, lon);
      
      fillRestaurants(nearest);
    } catch (RemoteException e) {
      e.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
  }

  public void loadBookmarked()
  {
    if(loadMoreButton != null) {
      loadMoreButton.setVisible(false);
      loadMoreButton.setManaged(false);
    }
    closeDetails();
    closeComment();

    title.setText("Ristoranti preferiti");
    listOfRestaurants.getChildren().clear();

    User user = Session.getCurrentUser();

    try{
      double lat = user.getLatitude();
      double lon = user.getLongitude();

      List<Restaurant> bookmarked = ServerConnection.getServer().getBookmarkedRestaurants(user.getId(), lat, lon);
      fillRestaurants(bookmarked);
    } catch (RemoteException e) {
      e.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
  }

  public void loadReviews()
  {
    if(loadMoreButton != null) {
      loadMoreButton.setVisible(false);
      loadMoreButton.setManaged(false);
    }
    closeDetails();
    closeComment();

    title.setText("Ristoranti recensiti");
    listOfRestaurants.getChildren().clear();

    User user = Session.getCurrentUser();

    try{
      double lat = user.getLatitude();
      double lon = user.getLongitude();

      List<Restaurant> reviewed = ServerConnection.getServer().getReviewedRestaurants(user.getId(), lat, lon);
      fillReviewed(reviewed);
    } catch (RemoteException e) {
      e.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
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

    filterCuisine = null;
    filterPrice = null;
    filterDelivery = null;
    filterBooking = null;
    filterStars = null;
    currentSearchPlace = place;
    currentSearchOffset = 0;

    title.setText("Ristoranti a " + place);
    listOfRestaurants.getChildren().clear();
    loadMoreButton.setVisible(false);
    loadMoreButton.setManaged(false);

    User user = Session.getCurrentUser();

    try{
      double lat = user.getLatitude();
      double lon = user.getLongitude();

      List<Restaurant> searchResults = ServerConnection.getServer().getSerachedRestaurants(currentSearchPlace, filterCuisine, filterPrice, filterDelivery, filterBooking, filterStars, currentSearchOffset, lat, lon);
      fillRestaurants(searchResults);

      if(searchResults.size() == 10) {
        loadMoreButton.setVisible(true);
        loadMoreButton.setManaged(true);
      }
    } catch (RemoteException e) {
      e.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
  }

  public void applyFilters(String cuisine, String price, String delivery, String booking, String stars)
  {
    closeDetails();
    closeComment();
    
    filterCuisine = cuisine;
    filterPrice = price;
    filterDelivery = delivery;
    filterBooking = booking;
    filterStars = stars;
    currentSearchOffset = 0;

    if (currentSearchPlace != null && !currentSearchPlace.isEmpty()) title.setText("Ristoranti filtrati a " + currentSearchPlace);
    else  title.setText("Ristoranti filtrati");
    listOfRestaurants.getChildren().clear();
    loadMoreButton.setVisible(false);
    loadMoreButton.setManaged(false);
    
    User user = Session.getCurrentUser();

    try{
      double lat = user.getLatitude();
      double lon = user.getLongitude();

      List<Restaurant> searchResults = ServerConnection.getServer().getSerachedRestaurants(currentSearchPlace, filterCuisine, filterPrice, filterDelivery, filterBooking, filterStars, currentSearchOffset, lat, lon);
      fillRestaurants(searchResults);

      if(searchResults.size() == 10) {
        loadMoreButton.setVisible(true);
        loadMoreButton.setManaged(true);
      }
    } catch (RemoteException e) {
      e.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
  }

  @FXML
  public void loadMoreClicked(ActionEvent e)
  {
    currentSearchOffset += 10; 
    
    User user = Session.getCurrentUser();

    try{
      double lat = user.getLatitude();
      double lon = user.getLongitude();

      List<Restaurant> nextResults = ServerConnection.getServer().getSerachedRestaurants(currentSearchPlace, filterCuisine, filterPrice, filterDelivery, filterBooking, filterStars, currentSearchOffset, lat, lon);
      fillRestaurants(nextResults);

      if(nextResults.size() < 10) {
        loadMoreButton.setVisible(false);
        loadMoreButton.setManaged(false);
      }
    } catch (RemoteException ex) {
      ex.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
  }

  private void fillRestaurants(List<Restaurant> restaurants) 
  {
    boolean isEmpty = restaurants.isEmpty();
    emptyLabel.setVisible(isEmpty);
    emptyLabel.setManaged(isEmpty);
    listOfRestaurants.setVisible(!isEmpty);
    if(isEmpty) return;

    for(Restaurant r : restaurants) {
      try {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/user/userRestaurantsRow.fxml"));
        HBox row = loader.load();

        UserRestaurantsRowController controller = loader.getController();
        controller.setRestaurant(r);

        listOfRestaurants.getChildren().add(row);
       }catch (IOException e) {
        System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading user restaurants");
        e.printStackTrace();
       }
    }
  }

  private void fillReviewed(List<Restaurant> restaurants)
  {
    listOfRestaurants.getChildren().clear();
    
    if (Session.getCurrentUser() == null) return;
    int userId = Session.getCurrentUser().getId();

    boolean isEmpty = restaurants.isEmpty();
    emptyLabel.setVisible(isEmpty);
    emptyLabel.setManaged(isEmpty);
    listOfRestaurants.setVisible(!isEmpty);
    if(isEmpty) return;


    for(Restaurant r : restaurants) {
      String[] restaurantData = {r.getName(), r.getAddress(), String.format("%.1f", r.getAverageStars()), String.valueOf(r.getReviewsNum()), String.valueOf(r.getId())};
      
      String[] myReview = null;
      try {
          myReview = ServerConnection.getServer().getUserReview(userId, r.getId());
      } catch (RemoteException ex) {
        ex.printStackTrace();
        System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
        continue;
      }
      
      String answer = (myReview[2] != null) ? myReview[2] : "";
      String[] reviewData = {myReview[0], myReview[1], answer};

      try {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/user/restaurantReviewsRow.fxml"));
        HBox row = loader.load();

        RestaurantReviewsRowController controller = loader.getController();
        controller.setReview(restaurantData, reviewData);

        listOfRestaurants.getChildren().add(row);
      }catch (IOException e) {
        System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading reviewed restaurants");
        e.printStackTrace();
      }
    }
  }

  public void openDetails(Restaurant restaurant)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/user/detailsUser.fxml"));
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
    if(detailsNode != null) {
      leftMenuArea.getChildren().remove(detailsNode);
      detailsNode = null;
    }
    
    listContainer.setVisible(true); 
  }

  public void viewComment(String[] comment)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/user/viewComment.fxml"));
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
    if(commentNode != null) {
      leftMenuArea.getChildren().remove(commentNode);
      commentNode = null;
    }
    
    listContainer.setVisible(true); 
  }

  public void openWriteComment(Restaurant restaurant)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/user/writeComment.fxml"));
      commentNode = loader.load();

      WriteCommentController controller = loader.getController();
      controller.setRestaurantReview(restaurant);

      if (detailsNode != null) detailsNode.setVisible(false);
      leftMenuArea.getChildren().add(commentNode); 

    }catch(IOException e) {
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading write comment page");
      e.printStackTrace();
    }
  }

  public void closeWriteComment()
  {
    if(commentNode != null) {
      leftMenuArea.getChildren().remove(commentNode);
      commentNode = null;
    }
    
    if (detailsNode != null) detailsNode.setVisible(true);
  }
}