/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.access;

import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.controller.user.UserHomeController;
import com.lab.utility.ErrorContainer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * GuestController gestisce l'interfaccia di accesso per gli utenti non registrati(ospiti) dell'applicazione TheKnife.
 * <p>
 * Permette all'ospite di inserire un luogo dove cercare i ristoranti per avviare l'applicazione senza eseguire l'accesso.
 * L'input viene validificato e in seguito caricata la homepage e la gestione viene passata al controller {@link UserHomeController}
 * </p> 
 */
public class GuestController {
  
  @FXML private TextField place_TF;
  @FXML private Button searchButton;

  /**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Configura l'interfaccia grafica iniziale, impostando:
   * - titolo della pagina
   * - a quale pagine si riferisce il bottone "torna indietro" della toolbar
   * - disattiva le opzioni di di accesso, registrazione e uscita dal profilo della toolbar
   * - imposta l'azione al premersi del tasto invio
   * - reimposta i colori standard dei bordi dell'input
   * </p>
   */
  @FXML
  public void initialize()
  {
    PageController.showTitle(true);
    ToolbarController.setupBackButton(true, "/com/lab/fxml/basic/home.fxml");
    ToolbarController.showLeftSide(false, false, false);
    place_TF.setOnAction(this::searchClicked);
    ErrorContainer.resetBorder(place_TF);
  }

  /**
   * Gestisce l'evento di conferma della ricerca da parte dell'ospite.
   * <p>
   * Verifica che l'input non sia vuoto. In caso di errore, applica un bordo di segnalazione tramite {@link ErrorContainer}.
   * Se la validificazione va a buon fine, memorizza il luogo e reinderizza l'utente alla homepage.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Salva oppure dalla pressione del tasto invio
   */
  @FXML
  public void searchClicked(ActionEvent event)
  {
    boolean error = false;

    String place = place_TF.getText().trim();
    if(place.isEmpty()) {
      ErrorContainer.errorBorder(place_TF);
      error = true;
    }

    if(error) return;
    UserHomeController.guestSearchPlace = place;
    PageController.selectPage("/com/lab/fxml/user/userHome.fxml");
  }
}
