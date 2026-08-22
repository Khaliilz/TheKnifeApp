package com.lab.controller.user;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javafx.event.ActionEvent;

import com.lab.App;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.model.Restaurant;
import com.lab.model.Session;
import com.lab.model.User;
import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;
import com.lab.utility.ErrorContainer;

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

import javafx.application.Platform;

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
  private DetailsUserController currentDetailsController;
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
  private int currentLoadId = 0;
  
  private enum UserState {
    NEAREST,
    BOOKMARKED,
    REVIEWS,
    SEARCH
  }
  private UserState currentState = UserState.NEAREST;

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

  public void refreshCurrentList()
  {
    switch(currentState) {
      case NEAREST: {
        loadNearest();
        break;
      }
      case BOOKMARKED: {
        loadBookmarked();
        break;
      }
      case REVIEWS: {
        loadReviews();
        break;
      }
      case SEARCH: {
        listOfRestaurants.getChildren().clear(); 
        currentSearchOffset = 0;
        executeSearch();
        break;
      }
    }
  }

  public void loadNearest()
  {
    currentState = UserState.NEAREST;

    if(loadMoreButton != null) {
      loadMoreButton.setVisible(false);
      loadMoreButton.setManaged(false);
    }
    title.setText("Ristoranti nelle vicinanze");
    listOfRestaurants.getChildren().clear();

    User user = Session.getCurrentUser();
    double lat = user.getLatitude();
    double lon = user.getLongitude();
    final int loadId = ++currentLoadId;

    CompletableFuture.supplyAsync(() -> {
      try{
        return ServerConnection.getServer().getNearestRestaurants(lat, lon);
      } catch (RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dei ristoranti vicini");
        return null;
      }
    }).thenAccept(nearest -> {
      Platform.runLater(() -> {
        if(loadId != currentLoadId) return;
        if(nearest != null) fillRestaurants(nearest);
        else {
          emptyLabel.setText("Errore di connessione con il server");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        }
      });
    });
  }

  public void loadBookmarked()
  {
    currentState = UserState.BOOKMARKED;

    if(loadMoreButton != null) {
      loadMoreButton.setVisible(false);
      loadMoreButton.setManaged(false);
    }
    
    closeWithoutRefresh();

    title.setText("Ristoranti preferiti");
    listOfRestaurants.getChildren().clear();

    User user = Session.getCurrentUser();
    double lat = user.getLatitude();
    double lon = user.getLongitude();
    final int loadId = ++currentLoadId;

    CompletableFuture.supplyAsync(() -> {
      try{
        return ServerConnection.getServer().getBookmarkedRestaurants(user.getId(), lat, lon);
      } catch (RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dei ristoranti preferiti");
        return null;
      }
    }).thenAccept(bookmarked -> {
      Platform.runLater(() -> {
        if(loadId != currentLoadId) return;
        if(bookmarked != null) fillRestaurants(bookmarked);
        else {
          emptyLabel.setText("Errore di connessione con il server");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        }
      });
    });
  }

  public void loadReviews()
  {
    currentState = UserState.REVIEWS;

    if(loadMoreButton != null) {
      loadMoreButton.setVisible(false);
      loadMoreButton.setManaged(false);
    }
    
    closeWithoutRefresh();

    title.setText("Ristoranti recensiti");
    listOfRestaurants.getChildren().clear();

    User user = Session.getCurrentUser();
    double lat = user.getLatitude();
    double lon = user.getLongitude();
    final int loadId = ++currentLoadId;

    CompletableFuture.supplyAsync(() -> {
      try{
        return ServerConnection.getServer().getReviewedRestaurants(user.getId(), lat, lon);
      } catch (RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dei ristoranti recensiti");
        return null;
      }
    }).thenAccept(reviewed -> {
      Platform.runLater(() -> {
        if(loadId != currentLoadId) return;
        if(reviewed != null) fillReviewed(reviewed);
        else {
          emptyLabel.setText("Errore di connessione con il server");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        }
      });
    });
  }

  public void loadRightMenu(String newTitle, String fileName)
  {
    try{
      title.setText(newTitle);
      Parent selectedMenu = FXMLLoader.load(App.class.getResource(fileName));
      rightMenuArea.getChildren().setAll(selectedMenu);
    }catch(IOException e) {
      System.out.print("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento del menu destro");
      e.printStackTrace();
    }
  }

  public void searchByPlace(String place)
  {
    currentState = UserState.SEARCH;

    closeWithoutRefresh();

    filterCuisine = null;
    filterPrice = null;
    filterDelivery = null;
    filterBooking = null;
    filterStars = null;
    currentSearchPlace = place;
    currentSearchOffset = 0;

    listOfRestaurants.getChildren().clear();
    title.setText("Ristoranti a " + place);

    executeSearch();
  }

  public void applyFilters(String cuisine, String price, String delivery, String booking, String stars)
  {
    closeWithoutRefresh();
    
    filterCuisine = cuisine;
    filterPrice = price;
    filterDelivery = delivery;
    filterBooking = booking;
    filterStars = stars;
    currentSearchOffset = 0;

    if (currentSearchPlace != null && !currentSearchPlace.isEmpty()) title.setText("Ristoranti filtrati a " + currentSearchPlace);
    else  title.setText("Ristoranti filtrati");
    listOfRestaurants.getChildren().clear();

    executeSearch();
  }

  @FXML
  public void loadMoreClicked(ActionEvent event)
  {
    currentSearchOffset += 10; 
    
    executeSearch();
  }

  private void executeSearch()
  {
    loadMoreButton.setVisible(false);
    loadMoreButton.setManaged(false);

    User user = Session.getCurrentUser();
    double lat = user.getLatitude();
    double lon = user.getLongitude();
    final int loadId = ++currentLoadId;

    CompletableFuture.supplyAsync(() -> {
      try{
        return ServerConnection.getServer().getSerachedRestaurants(currentSearchPlace, filterCuisine, filterPrice, filterDelivery, filterBooking, filterStars, currentSearchOffset, lat, lon);
      } catch (RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dei ristoranti per: " + currentSearchPlace);
        return null;
      }
    }).thenAccept(searchResults -> {
      Platform.runLater(() -> {
        if(loadId != currentLoadId) return;
        if(searchResults != null) {
          fillRestaurants(searchResults);

          if(searchResults.size() == 10) {
            loadMoreButton.setVisible(true);
            loadMoreButton.setManaged(true);
          } else {
            loadMoreButton.setVisible(false);
            loadMoreButton.setManaged(false);
          }
        } else {
          emptyLabel.setText("Errore di connessione con il server");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        }
      });
    });
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
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento dei ristoranti");
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

      CompletableFuture.supplyAsync(() -> {
        try{
          return ServerConnection.getServer().getUserReview(userId, r.getId());
        } catch (RemoteException ex) {
          ex.printStackTrace();
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta recensioni");
          return null;
        }
      }).thenAccept(myReview -> {
        Platform.runLater(() -> {
          if(myReview != null) {
            String answer = (myReview[2] != null) ? myReview[2] : "";
            String[] reviewData = {myReview[0], myReview[1], answer};

            try {
              FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/user/restaurantReviewsRow.fxml"));
              HBox row = loader.load();

              RestaurantReviewsRowController controller = loader.getController();
              controller.setReview(restaurantData, reviewData);

              listOfRestaurants.getChildren().add(row);
            }catch (IOException e) {
              System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento recensioni");
              e.printStackTrace();
            }
          }
        });
      });
    }
  }

  public void openDetails(Restaurant restaurant)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/user/detailsUser.fxml"));
      detailsNode = loader.load();

      currentDetailsController = loader.getController();
      currentDetailsController.setDetails(restaurant);

      listContainer.setVisible(false); 
      leftMenuArea.getChildren().add(detailsNode); 

    }catch(IOException e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento dettagli del ristorante");
    }
  }

  public void closeDetails()
  {
    closeWithoutRefresh();
    refreshCurrentList();
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
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento recensioni del ristorante");
    }
  }

  public void closeComment()
  {
    closeWithoutRefresh();
    refreshCurrentList();
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
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento form di scrittura commento");
    }
  }

  public void closeWriteComment()
  {
    if(commentNode != null) {
      leftMenuArea.getChildren().remove(commentNode);
      commentNode = null;
    }
    
    if(detailsNode != null) {
      detailsNode.setVisible(true);
      if(currentDetailsController != null) currentDetailsController.refreshReviews();
    }
  }

  public void closeWithoutRefresh()
  {
    if(detailsNode != null) {
      leftMenuArea.getChildren().remove(detailsNode);
      detailsNode = null;
    }
    if(commentNode != null) {
      leftMenuArea.getChildren().remove(commentNode);
      commentNode = null;
    }
    listContainer.setVisible(true);
    listContainer.requestFocus();
  }
}