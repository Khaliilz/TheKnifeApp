/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.restaurateur;

import com.lab.model.Restaurant;
import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import java.rmi.RemoteException;

/**
 * YourRestaurantsController Gestisce l'interfaccia per la singola riga della lista del ristorante del ristoratore.
 * <p>
 * Questa classe si occupa di formattare i dati ricevuti dal server e di inserirli nei componenti grafici della riga relativa alle informazioni del ristorante.
 * Inoltre gestisce lla richiesta di visualizzazione dei dettagli del ristorante da parte del ristoratore, e dell'eliminazione di un ristorante del ristoratore.
 * </p>
 */
public class YourRestaurantsController {
  
  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text reviewsNum;
  @FXML private Button removeButton;

  private Restaurant currentRestaurant;
  
  /**
   * Spacchetta le singole informazioni del ristorante ottenuto tramite parametro, li carica nei componenti grafici dedicati e se necessario li formatta adeguatamente.
   * 
   * @param r Ristorante corrente da cui estrarre i dati da formattare e caricare.
   */
  public void setRestaurantData(Restaurant r)
  {
    currentRestaurant = r;

    name.setText(r.getName());
    
    String fullAddress = r.getAddress();
    String shortAddress = fullAddress;
    if(fullAddress != null && fullAddress.contains(",")){
      String[] split = fullAddress.split(",");
      if(split.length >= 3) shortAddress = split[0].trim() + ", " + split[1].trim() + ", " + split[2].trim(); 
    }
    address.setText(shortAddress);
    
    starsNum.setText(String.format("%.1f", r.getAverageStars()));
    reviewsNum.setText(String.valueOf(r.getReviewsNum()));
  }

  /**
   * Apre la visualizzazione dei dettagli del ristorante corrente tramite il metodo {@link RestaurateurHomeController#openDetails(Restaurant)}
   * 
   * @param event L'evento scatenato dal click sul bottone Dettagli.
   */
  @FXML public void detailClicked(ActionEvent event)
  {
    RestaurateurHomeController.getInstance().openDetails(currentRestaurant);
  }

  /**
   * Disabilita temporaneamente il controllo del thread grafico, per lanciare un thread asincrono che chiede al server remoto di eseguire l'operazione di rimozione del ristorante dal database.
   * Una volta ricevuta risposta, il thread grafico riprende il controllo riaggiornando la lista dei ristoranti posseduti.
   * 
   * @param event L'evento scatenato dal click sul bottone Rimuovi.
   */
  @FXML public void removeClicked(ActionEvent event)
  {
    removeButton.setDisable(true);

    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().removeRestaurant(currentRestaurant.getId());
      } catch(RemoteException ex) {
        ex.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta di rimozione ristorante");
        return false;
      }
    }).thenAccept(success -> {
      Platform.runLater(() -> {
        removeButton.setDisable(false);
        if(!success) System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta di rimozione ristorante");
        else {
          System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Ristorante rimosso");
          RestaurateurHomeController.getInstance().fillRestaurants();
        }
      });
    });
  }
}
