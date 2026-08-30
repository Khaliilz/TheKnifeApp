/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.restaurateur;

import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

import com.lab.model.Session;
import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;
import com.lab.utility.ErrorContainer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * NewRestaurantController Gestisce l'interfaccia per l'aggiunta di un nuovo ristorante per il ristoratore.
 * <p>
 * Questa classe si occupa di ottenere i dati di un ristorante, di verificarne la correttezza e di inviarli al server per il salvataggio.
 * </p>
 */
public class NewRestaurantController {
  
  @FXML private TextField name;
  @FXML private TextField address;
  @FXML private TextField city;
  @FXML private TextField country;
  @FXML private TextField latitude;
  @FXML private TextField longitude;
  @FXML private TextField cuisine;
  @FXML private TextField websiteUrl;
  @FXML private TextField phoneNumber; 
  @FXML private ToggleGroup priceGroup;
  @FXML private Button saveButton;

  /**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Ripristina la corretta visualizzazione dello stile degli oggetti relativi alla raccolta degli input dell'utente
   * </p>
   */
  @FXML
  public void initialize()
  {
    ErrorContainer.resetBorder(name);
    ErrorContainer.resetBorder(address);
    ErrorContainer.resetBorder(city);
    ErrorContainer.resetBorder(country);
    ErrorContainer.resetBorder(latitude);
    ErrorContainer.resetBorder(longitude);
    ErrorContainer.resetBorder(cuisine);
    ErrorContainer.resetBorder(websiteUrl);
    ErrorContainer.resetBorder(phoneNumber);
  }

  /**
   * Raccoglie i dati in input del ristoratore, verifica la loro correttezza, in caso di errori, viene segnalato tramite la classe {@link ErrorContainer}.
   * Una volta verificata la correttezza dei dati, viene inoltrata una richiesta di registrazione del ristorante al server remoto, tramite un thead in esecuzione in background.
   * Una volta ricevuta la risposta, la schermata viene chiusa, mostrando la lista dei propri ristoranti.
   * 
   * @param event L'evento scatenato dal click sul bottone Salva.
   */
  @FXML
  public void saveClicked(ActionEvent event)
  {
    boolean error = false;

    String nameR = name.getText().trim();
    String addressR = address.getText().trim();
    String cityR = city.getText().trim();
    String countryR = country.getText().trim();
    String latitudeR = latitude.getText().trim();
    String longitudeR = longitude.getText().trim();
    String cuisineR = cuisine.getText().trim();
    String websiteUrlR = websiteUrl.getText().trim();
    String phoneNumberR = phoneNumber.getText().trim();
    String price = ((RadioButton) priceGroup.getSelectedToggle()).getText();

    if(nameR.isEmpty()) {
      ErrorContainer.errorBorder(name);
      error = true;
    }

    String addressRegex = "^[^,]+?\\s+\\d+[a-zA-Z]?$";
    if(addressR.isEmpty() || !addressR.matches(addressRegex)) {
      ErrorContainer.errorBorder(address);
      error = true;
    }

    String cityRegex = "^[\\p{L}\\s\\'\\-]+,\\s*[\\p{L}\\s\\'\\-]+$";
    if(cityR.isEmpty() || !cityR.matches(cityRegex)) {
      ErrorContainer.errorBorder(city);
      error = true;
    }

    String countryRegex = "^[\\p{L}\\s\\'\\-\\.]+$";
    if(countryR.isEmpty() || !countryR.matches(countryRegex)) {
      ErrorContainer.errorBorder(country);
      error = true;
    }

    String decimalRegex = "^-?\\d+(\\.\\d+)?$";
    if(latitudeR.isEmpty() || !latitudeR.matches(decimalRegex)) {
      ErrorContainer.errorBorder(latitude);
      error = true;
    } else {
      double lat = Double.parseDouble(latitudeR);
      if(lat < -90.0 || lat > 90.0) {
        ErrorContainer.errorBorder(latitude);
        error = true;
      }
    }

    if(longitudeR.isEmpty() || !longitudeR.matches(decimalRegex)) {
      ErrorContainer.errorBorder(longitude);
      error = true;
    } else {
      double lon = Double.parseDouble(longitudeR);
      if(lon < -180.0 || lon > 180.0) {
        ErrorContainer.errorBorder(longitude);
        error = true;
      }
    }

    String cuisineRegex = "^[\\p{L}\\s\\'\\-]+(,\\s*[\\p{L}\\s\\'\\-]+)*$";
    if(cuisineR.isEmpty() || !cuisineR.matches(cuisineRegex)) {
      ErrorContainer.errorBorder(cuisine);
      error = true;
    }

    String phoneRegex = "^\\+\\d{8,15}$";
    if(!phoneNumberR.isEmpty() && !phoneNumberR.matches(phoneRegex)) {
      ErrorContainer.errorBorder(phoneNumber);
      error = true;
    }

    if(error) return;

    String fullAddress = addressR + ", " + cityR + ", " + countryR;
    int ownerId = Session.getCurrentUser().getId();
    
    saveButton.setDisable(true);
    saveButton.setText("SALVATAGGIO...");

    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().addRestaurant(nameR, fullAddress, cuisineR, price, phoneNumberR, websiteUrlR, Double.parseDouble(latitudeR), Double.parseDouble(longitudeR), ownerId);
      } catch(RemoteException ex) {
        ex.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta salvataggio dati nuovo ristorante");
        return false;
      }
    }).thenAccept(success -> {
      Platform.runLater(() -> {
        saveButton.setDisable(false);
        saveButton.setText("SALVA");
        if(!success) System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta salvataggio dati nuovo ristorante");
        else {
          System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Nuovo ristorante salvato");
          RestaurateurHomeController.getInstance().closeNewRestaurant();
        }
      });
    });
  }

  /**
   * Gestisce l'evento cancella dalla schermata di aggiunta del ristorante per il ristoratore.
   * <p>
   * Nel caso il ristoratore non volesse piu' aggiungere un ristorante, gli e' permesso tornare indietro alla visualizzazione dei propri ristoranti.
   * Questo viene fatto chiamando il metodo {@link RestaurateurHomeController#closeNewRestaurant()}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Cancella.
   */
  @FXML
  public void cancelClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Cancel button clicked");
    RestaurateurHomeController.getInstance().closeNewRestaurant();
  }
}
