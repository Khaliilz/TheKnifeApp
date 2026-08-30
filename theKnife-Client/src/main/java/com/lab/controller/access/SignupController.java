/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.access;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * SignupController Gestisce l'interfaccia di registrazione dell'utente alla piattaforma.
 * <p>
 * Questa classe si occupa di raccogliere le credenziali inserite dall'utente e di comunicare con il server tramite protocollo RMI per la registrazione.
 * L'operazione di rete viene gestita in modo asincrono per non bloccare l'interfaccia grafica.
 * In caso di successo, la classe esegue un accesso automatico, inizializza la sessione e reindirizza l'utente alla homepage corrispondente al proprio ruolo.
 * </p>
 */
public class SignupController {
  
  @FXML private TextField name_TF;
  @FXML private TextField surname_TF;
  @FXML private DatePicker birthDate;
  @FXML private TextField address_TF;
  @FXML private TextField username_TF;
  @FXML private PasswordField password_PF;
  @FXML private RadioButton customer;
  @FXML private RadioButton restaurateur;
  @FXML private Button signup_B;

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
    
    password_PF.setOnAction(this::signupClicked);
    ErrorContainer.resetBorder(name_TF);
    ErrorContainer.resetBorder(surname_TF);
    ErrorContainer.resetBorder(birthDate);
    ErrorContainer.resetBorder(address_TF);
    ErrorContainer.resetBorder(username_TF);
    ErrorContainer.resetBorder(password_PF);
  }

  /**
   * Gestisce l'evento di registrazione al sistema.
   * <p>
   * Verifica che gli input non siano vuoti e validi secondo delle regole (regex). In caso di errore, applica un bordo di segnalazione tramite {@link ErrorContainer}.
   * Se la validificazione va a buon fine, l'intefaccia viene temporaneamente disabilitata e viene avviato un thread in background per inviare la richiesta al server remoto.
   * Ricevuta la risposta, viene avviato un secondo thread in background per inviare la richiesta di accesso al server remote.
   * Una volta ricevuta la risposta, il controllo ritorna al thread grafico, e se le credenziali sono correte viene salvata la sessione e l'utente viene indirizzato alla sua homepage dedicata.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Accedi o dalla pressione del tasto invio.
   */
  @FXML
  public void signupClicked(ActionEvent event)
  {
    boolean error = false;

    String name = name_TF.getText().trim();
    String surname = surname_TF.getText().trim();

    LocalDate date = birthDate.getValue();
    if(date == null && !birthDate.getEditor().getText().trim().isEmpty()) {
      try{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date = LocalDate.parse(birthDate.getEditor().getText().trim(), formatter);
        birthDate.setValue(date);
      }catch(Exception e) {
        date = null;
      }
    }
    
    String address = address_TF.getText().trim().toLowerCase();
    String username = username_TF.getText();
    String password = password_PF.getText();
    String role = customer.isSelected()? "CLIENTE" : "RISTORATORE";

    if(name.isEmpty()) { 
      ErrorContainer.errorBorder(name_TF);
      error = true;
    }

    if(surname.isEmpty()) {
      ErrorContainer.errorBorder(surname_TF);
      error = true;
    }

    Date sqlDate = null;
    if(date == null || !checkBirthDate(date)) {
      ErrorContainer.errorBorder(birthDate);
      error = true;
    } else {
      sqlDate = Date.valueOf(date);
    }

    String addressRegex = "^[\\p{L}\\s\\'\\-\\.]+\\s*,\\s*[\\p{L}\\s\\'\\-\\.]+$";
    if(address.isEmpty() || !address.matches(addressRegex)) {
      ErrorContainer.errorBorder(address_TF);
      error = true;
    }

    if(username.isEmpty()) {
      ErrorContainer.errorBorder(username_TF);
      error = true;
    }

    if(password.isEmpty() || password.length() < 8) { 
      ErrorContainer.errorBorder(password_PF); 
      error = true; 
    }

    if(error) return;

    signup_B.setDisable(true);
    signup_B.setText("REGISTRAZIONE...");

    final Date SQLDATE = sqlDate;

    CompletableFuture.supplyAsync(() -> {
      try {
        return ServerConnection.getServer().signup(name, surname, SQLDATE, address, username, password, role);
      } catch(RemoteException e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dati di registrazione");
        return false;
      }
    }).thenAccept(signupSuccess -> {
      Platform.runLater(() -> {
        signup_B.setDisable(false);
        signup_B.setText("REGISTRATI");

        if(!signupSuccess) ErrorContainer.errorBorder(username_TF);
        else {
          System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Registrazione completata");

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
              if(user == null) {
                System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta dati di accesso");
              } else {
                System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Accesso eseguito");
                Session.setCurrentUser(user);

                if(user.getRole().equals("CLIENTE")) PageController.selectPage("/com/lab/fxml/user/userHome.fxml");
                else if(user.getRole().equals("RISTORATORE")) PageController.selectPage("/com/lab/fxml/restaurateur/restaurateurHome.fxml");
              }
            });
          });
        }
      });
    });
  }

  /**
   * Controlla la correttezza della data di nascita dell'utente.
   * <p>
   * Verifica che l'input sia valido, controllando che:
   * - la data non sia superiore ad oggi
   * - l'utente non abbia un eta' superiore a 89 anni o inferiore a 16
   * </p>
   * 
   * @param d La data di nascita impostata dall'utente.
   */
  private boolean checkBirthDate(LocalDate d)
  {
    LocalDate today = LocalDate.now();
    long age = java.time.temporal.ChronoUnit.YEARS.between(d, today);

    if(d.isAfter(today)) return false;
    if(age > 90 || age < 16) return false;

    return true;
  }
}
