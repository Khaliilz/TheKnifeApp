/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.restaurateur;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import java.util.List;

import com.lab.App;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.model.Session;
import com.lab.network.ServerConnection;
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
import javafx.scene.Node;

/**
 * RestaurateurHomeController Gestisce l'interfaccia relativa alla homepage del ristoratore.
 * <p>
 * Questa classe si occupa caricare graficamente la lista dei propri ristoranti e delle recensioni relative ad essi.
 * Inoltre si occupa della chiusura e apertura dell finestre di dialogo per la visualizzazione dei dettagli del ristorante e per la risposta ad una recensione.
 * </p>
 */
public class RestaurateurHomeController {
  
  @FXML private Text title;
  @FXML private VBox list;
  @FXML private StackPane contentArea;
  @FXML private ScrollPane listContainer;
  @FXML private Label emptyLabel;
  @FXML private VBox mainArea;

  private Node detailsNode;
  private Node newRestaurantNode;

  private static RestaurateurHomeController instance;

  /**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Instanzia la classe a se stessa, per poterla richiamare staticamente nelle altre schermate dipendenti da essa.
   * Nasconde il titolo originale dell'applicazione.
   * Nasconde il bottone indietro della toolbar.
   * Mostra il bottone di uscita dal profilo ristoratore.
   * </p>
   */
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

  /**
   * Restituisce l'istanza della classe per poter richiamare i metodi forniti dalla homepage del ristoratore.
   */
  public static RestaurateurHomeController getInstance()
  {
    return instance;
  }

  /**
   * Imposta il titolo della pagina in base al valore passato per parametro.
   * 
   * @param t Titolo della pagina
   */
  public void setTitle(String t)
  {
    title.setText(t);
  }

  /**
   * Effettua una chiamata al server remoto, in un thread asincorno in background, richiedendo la lista dei ristoranti del ristoratore, tramite id ottenuto dalla sessione in corso.
   * Una volta ottenuta la risposta, la lista di ristoranti viene spacchettata, e il thread grafico riprende il controllo impostando i singoli valori degli oggetti grafici e aggiungendo gli elementi alla lista di ristoranti grafica.
   */
  public void fillRestaurants()
  {
    list.getChildren().clear();

    if (Session.getCurrentUser() == null) return;
    int ownerId = Session.getCurrentUser().getId();

    CompletableFuture.supplyAsync(() -> {
      try{
        return ServerConnection.getServer().getRestaurantsByOwner(ownerId);
      } catch (RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dei tuoi ristoranti");
        return null;
      }
    }).thenAccept(restaurants -> {
      Platform.runLater(() -> {
        if(restaurants == null) {
          System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dei tuoi ristoranti");
          emptyLabel.setText("Errore di connessione con il server");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        } else if(restaurants.isEmpty()) {
          emptyLabel.setText("Nessun ristorante presente al momento");
          emptyLabel.setVisible(true);
          emptyLabel.setManaged(true);
        } else {
          emptyLabel.setVisible(false);
          emptyLabel.setManaged(false);
          for(Restaurant r : restaurants) {
            try{
              FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/restaurateur/yourRestaurants.fxml"));
              HBox row = loader.load();

              YourRestaurantsController controller = loader.getController();
              controller.setRestaurantData(r);

              list.getChildren().add(row);
            }catch(IOException e) {
              System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento dei tuoi ristoranti");
              e.printStackTrace();
            }
          }
        }
      });
    });
  }

  /**
   * Chiama il metodo {@link DetailsRestaurateurController#setRestaurant(Restaurant)} mandando il ristorante dal quale si vogliono vedere le recensioni.
   * Viene caricata una finestra fittizzia soprastante alla homepage del ristoratore in modo tale da ridurre i tempi di caricamento della finestra.
   * Al termine verra' distrutta e si tornera' alla visualizzazione di tutti i ristoranti.
   *  
   * @param r Ristorante corrente da visualizzare nel dettaglio
   */
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
    }catch(IOException e) {
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento dettagli del tuo ristorante");
      e.printStackTrace();
    }
  }

  /**
   * Viene chiusa la finestra fittizzia caricata sopra la homepage del ristoratore e reimpostati i valori degli oggetti standard della homepage.
   * Infine, visto che si e' scelto di usare questa tecnica, vengono ricaricati i ristoranti del ristoratore in modo da essere aggiornati con le modifiche piu' recenti.
   */
  public void closeDetails()
  {
    if(detailsNode != null) {
      contentArea.getChildren().remove(detailsNode);
      detailsNode = null;
    }

    setTitle("I tuoi ristoranti");
    mainArea.setVisible(true);
    mainArea.requestFocus();

    fillRestaurants();
  }

  /**
   * Viene caricata la risorsa, tramite link, della schermata per l'aggiunta di un nuovo ristorante.
   * La schermata viene caricata sopra la homepage del ristoratore.
   * 
   * @param event L'evento scatenato dal click sul bottone Aggiungi.
   */
  @FXML public void addClicked(ActionEvent event)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/restaurateur/newRestaurant.fxml"));
      newRestaurantNode = loader.load();

      setTitle("Nuovo Ristorante");

      mainArea.setVisible(false);
      contentArea.getChildren().add(newRestaurantNode);
    } catch(IOException ex) {
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Caricamento finestra form nuovo ristorante");
      ex.printStackTrace();
    }
  }

  /**
   * Viene chiusa la finestra fittizzia caricata sopra la homepage del ristoratore, per l'aggiunta di un nuovo ristorante, e reimpostati i valori degli oggetti standard della homepage.
   * Infine, visto che si e' scelto di usare questa tecnica, vengono ricaricati i ristoranti del ristoratore in modo da essere aggiornati con le modifiche piu' recenti.
   */
  public void closeNewRestaurant()
  {
    if(newRestaurantNode != null) {
      contentArea.getChildren().remove(newRestaurantNode);
      newRestaurantNode = null;
    }

    setTitle("I tuoi ristoranti");
    mainArea.setVisible(true);
    mainArea.requestFocus();

    fillRestaurants();
  }
}
