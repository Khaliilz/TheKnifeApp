/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.basic;

import com.lab.controller.access.GuestController;
import com.lab.controller.access.SignupController;
import com.lab.controller.user.UserHomeController;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * HomeController Gestisce l'interfaccia principale per la scelta della modalita' di accesso alla piattaforma.
 * <p>
 * Questa classe si occupa di indirizzare l'utente verso la schermata dedicata in base alla modalita' di accesso scelta.
 * </p>
 */
public class HomeController {
  
  @FXML private Button signin_B;
  @FXML private Button signup_B;
  @FXML private Button guest_B;

  /**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Configura l'interfaccia grafica iniziale, impostando:
   * - titolo della pagina
   * - disattiva il tasto "torna indietro" della toolbar
   * - disattiva le opzioni di di accesso, registrazione e uscita dal profilo della toolbar
   * </p>
   */
  public void initialize()
  {
    PageController.showTitle(true);
    ToolbarController.showBackButton(false);
    ToolbarController.showLeftSide(false, false, false);
  }

  /**
   * Gestisce l'evento di accesso alla pagina di accesso al sistema.
   * <p>
   * Direzione l'utente alla pagina di accesso al sistema {@link com.lab.controller.access.SigninController}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Accedi.
   */
  @FXML
  public void signinClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Signin button clicked");
    PageController.selectPage("/com/lab/fxml/access/signin.fxml");
  }

  /**
   * Gestisce l'evento di accesso alla pagina di registrazione al sistema.
   * <p>
   * Direzione l'utente alla pagina di registrazione al sistema {@link com.lab.controller.access.SignupController}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Registrati.
   */
  @FXML
  public void signupClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Signup button clicked");
    PageController.selectPage("/com/lab/fxml/access/signup.fxml");
  }

  /**
   * Gestisce l'evento di accesso alla pagina di accesso per ospiti al sistema.
   * <p>
   * Direzione l'utente alla pagina di accesso per ospiti al sistema {@link com.lab.controller.access.GuestController}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Ospite.
   */
  @FXML
  public void guestClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Guest button clicked");
    PageController.selectPage("/com/lab/fxml/access/guest.fxml");
  }
}
