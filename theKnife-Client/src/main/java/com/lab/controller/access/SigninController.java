/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.access;

import java.rmi.RemoteException;

import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.controller.user.UserHomeController;
import com.lab.model.Session;
import com.lab.model.User;
import com.lab.network.ServerConnection;
import com.lab.utility.StringColor;
import com.lab.utility.ErrorContainer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

/**
 * SigninController Gestisce l'interfaccia di accesso dell'utente registrato alla piattaforma.
 * <p>
 * Questa classe si occupa di raccogliere le credenziali inserite dall'utente e di comunicare con il server tramite protocollo RMI per l'autenticazione.
 * L'operazione di rete viene gestita in modo asincrono per non bloccare l'interfaccia grafica.
 * In caso di successo, la classe inizializza la sessione e reindirizza l'utente alla homepage corrispondente al proprio ruolo.
 * </p>
 */
public class SigninController {
  
  @FXML private Button signin_B;
  @FXML private TextField username_TF;
  @FXML private PasswordField password_PF;

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
    
    password_PF.setOnAction(this::signinClicked);
    ErrorContainer.resetBorder(username_TF);
    ErrorContainer.resetBorder(password_PF);
  }

  /**
   * Gestisce l'evento di accesso al sistema.
   * <p>
   * Verifica che l'input non sia vuoto. In caso di errore, applica un bordo di segnalazione tramite {@link ErrorContainer}.
   * Se la validificazione va a buon fine, l'intefaccia viene temporaneamente disabilitata e viene avviato un thread in background per inviare la richiesta al server remoto.
   * Ricevuta la risposta, il controllo ritorna al thread grafico, e se le credenziali sono correte viene salvata la sessione e l'utente viene indirizzato alla sua homepage dedicata.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Accedi o dalla pressione del tasto invio.
   */
  @FXML
  public void signinClicked(ActionEvent event)
  {
    boolean error = false;
    
    String username = username_TF.getText();
    String password = password_PF.getText();

    if(username.isEmpty()) {
      ErrorContainer.errorBorder(username_TF);
      error = true;
    }

    if(password.isEmpty()) { 
      ErrorContainer.errorBorder(password_PF); 
      error = true; 
    }

    if(error) return;

    signin_B.setDisable(true);
    signin_B.setText("ACCESSO...");

    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().signin(username, password);
      } catch(RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dati di accesso");
        return null;
      }
    }).thenAccept(user -> {
      Platform.runLater(() -> {
        signin_B.setDisable(false);
        signin_B.setText("ACCEDI");
        if(user == null) {
          ErrorContainer.errorBorder(username_TF);
          ErrorContainer.errorBorder(password_PF);
        } else {
          System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Accesso eseguito [" + user.getUsername() + "]");
          Session.setCurrentUser(user);

          if(user.getRole().equals("CLIENTE")) PageController.selectPage("/com/lab/fxml/user/userHome.fxml");
          else if(user.getRole().equals("RISTORATORE")) PageController.selectPage("/com/lab/fxml/restaurateur/restaurateurHome.fxml");
        }
      });
    });
  }
}
