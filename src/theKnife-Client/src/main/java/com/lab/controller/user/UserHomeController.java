/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.user;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javafx.scene.Node;
import javafx.event.ActionEvent;

import com.lab.App;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.model.Restaurant;
import com.lab.model.Session;
import com.lab.model.User;
import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;

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

/**
 * UserHomePage gestisce la homepage dell'utente e fornisce la maggior parte dei metodi che permettono all'utente di eseguire le azioni desiderate.
 * <p>
 * Questa classe agisce come controller centrale per la navigazione: gestisce sia la visualizzazione per l'utente registrato, sia quella per l'ospite.
 * Per ridurre la complessità del codice, l'interfaccia viene adattata nascondendo i componenti grafici non accessibili al ruolo corrente.
 * </p>
 */
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
  private Node detailsNode;
  private Node commentNode;

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

  /**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Instanzia se stessa.
   * Nasconde il titolo originale e il bottone di ritorno indietro della toolbar.
   * Valuta se l'utente è un ospite o un utente registrato, nascondendo i pulsanti non necessari.
   * Carica il menu di navigazione laterale.
   * Se l'ospite ha avviato una ricerca iniziale, la esegue automaticamente, altrimenti carica i ristoranti vicini.
   * </p>
   */
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

  /**
   * Restituisce l'istanza singleton corrente del controller.
   * 
   * @return L'istanza attiva di {@link UserHomeController}.
   */
  public static UserHomeController getInstance()
  {
    return instance;
  }

  /**
   * Ricarica la vista attuale interrogando nuovamente il server in base allo stato corrente.
   */
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

  /**
   * Scarica in modo asincrono la lista dei ristoranti nelle vicinanze dell'utente e aggiorna la grafica.
   */
  public void loadNearest()
  {
    currentState = UserState.NEAREST;
    currentSearchPlace = "";

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

  /**
   * Scarica in modo asincrono la lista dei ristoranti preferiti dell'utente registrato.
   */
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

  /**
   * Scarica in modo asincrono la lista dei ristoranti che l'utente registrato ha recensito in passato.
   */
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

  /**
   * Carica un nuovo menu contestuale nella barra laterale destra dell'interfaccia.
   * 
   * @param newTitle Il titolo da assegnare alla sezione caricata.
   * @param fileName L'indirizzo del menu da inserire.
   */
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

  /**
   * Inizializza e avvia una nuova ricerca di ristoranti basata esclusivamente sulla posizione inserita.
   * 
   * @param place La stringa rappresentante il luogo o l'indirizzo da cercare.
   */
  public void searchByPlace(String place)
  {
    if("vicino".equalsIgnoreCase(place.trim())) {
      loadNearest();
      return;
    }

    currentState = UserState.SEARCH;

    closeWithoutRefresh();

    filterCuisine = null;
    filterPrice = null;
    filterDelivery = null;
    filterBooking = null;
    filterStars = null;
    currentSearchPlace = place.trim();
    currentSearchOffset = 0;

    listOfRestaurants.getChildren().clear();
    title.setText("Ristoranti a " + place);

    executeSearch();
  }

  /**
   * Avvia una ricerca applicando vari filtri per restringere i risultati desiderati.
   * 
   * @param cuisine Tipologia di cucina.
   * @param price Fascia di prezzo selezionata.
   * @param delivery Richiesta disponibilità di consegna a domicilio.
   * @param booking Richiesta disponibilità di prenotazione online.
   * @param stars Media recensioni minima desiderata.
   */
  public void applyFilters(String cuisine, String price, String delivery, String booking, String stars)
  {
    closeWithoutRefresh();
    
    currentState = UserState.SEARCH;

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

  /**
   * Avanza l'offset di paginazione dei risultati e interroga nuovamente il database.
   * 
   * @param event L'evento scatenato dal click sul bottone Carica altri.
   */
  @FXML
  public void loadMoreClicked(ActionEvent event)
  {
    currentSearchOffset += 10; 
    
    executeSearch();
  }

  /**
   * Esegue la query di ricerca sul server combinando parametri di testo, offset e filtri attivi.
   * <p>
   * Gestisce dinamicamente la paginazione dei risultati, valutando se mostrare o nascondere il pulsante Carica altri.
   * </p>
   */
  private void executeSearch()
  {
    loadMoreButton.setVisible(false);
    loadMoreButton.setManaged(false);

    User user = Session.getCurrentUser();
    double lat = 0.0;
    double lon = 0.0;
    if(user != null) {
      lat = user.getLatitude();
      lon = user.getLongitude();
    }
    final int loadId = ++currentLoadId;
    final double userLat = lat;
    final double userLon = lon;

    CompletableFuture.supplyAsync(() -> {
      try{
        return ServerConnection.getServer().getSerachedRestaurants(currentSearchPlace, filterCuisine, filterPrice, filterDelivery, filterBooking, filterStars, currentSearchOffset, userLat, userLon);
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

  /**
   * Popola dinamicamente il contenitore grafico principale con la lista dei ristoranti ottenuta.
   * <p>
   * Per ogni ristorante della lista passata come parametro, vengono specificate le singole informazioni del ristorante chiamando il metodo {@link UserRestaurantRowController#setRestaurant(Restaurant)}
   * </p>
   * 
   * @param restaurants La lista dei ristoranti da inserire nella lista.
   */
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

  /**
   * Popola dinamicamente il contenitore grafico principale con la lista ristoranti recensiti ottenuta.
   * <p>
   * Per ogni ristorante della lista passata come parametro, vengono specificate le singole informazioni del ristorante chiamando il metodo {@link RestaurantReviewsRowController#setReview(String[], String[])}
   * </p>
   * 
   * @param restaurants La lista dei ristoranti da inserire nella lista.
   */
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

  /**
   * Apre la finestra contenente i dettagli informativi completi di uno specifico ristorante.
   * 
   * @param restaurant L'oggetto ristorante selezionato dalla lsita.
   */
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

  /**
   * Chiude la finestra dei dettagli del ristorante e ricarica i risultati correnti.
   */
  public void closeDetails()
  {
    closeWithoutRefresh();
    refreshCurrentList();
  }

  /**
   * Apre il form visivo per gestire la modifica o la lettura di un commento precedentemente inserito.
   * 
   * @param comment Array contenente le informazioni per la visualizzazione della recensione.
   */
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

  /**
   * Chiude la finestra del commento e ripristina la visualizzazione dell'elenco ristoranti.
   */
  public void closeComment()
  {
    closeWithoutRefresh();
    refreshCurrentList();
  }

  /**
   * Apre l'interfaccia dedicata alla scrittura di una nuova recensione per un ristorante.
   * 
   * @param restaurant Ristorante da recensire.
   */
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

  /**
   * Chiude la finestra per la scrittura del commento e riattiva il nodo dei dettagli del ristorante riaggiornando le recensioni.
   */
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

  /**
   * Metodo utility per svuotare graficamente i pannelli in sovrimpressione.
   */
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