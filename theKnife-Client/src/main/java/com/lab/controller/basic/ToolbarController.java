/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.basic;

import com.lab.model.Session;
import com.lab.utility.StringColor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import com.lab.network.ServerConnection;

/**
 * ToolbarController Gestisce l'interfaccia toolbar della piattaforma.
 * <p>
 * Questa classe si occupa di gestire la grafica degli oggetti presenti nella toolbar.
 * </p>
 */
public class ToolbarController {
  @FXML private StackPane toolbar;
  @FXML private Button exit_B;
  @FXML private Button minimize_B;
  @FXML private Button back_B;
  @FXML private Button signin_B;
  @FXML private Button signup_B;
  @FXML private Button signout_B;

  private static ToolbarController toolbarController;

  private double offsetX = 0;
  private double offsetY = 0;
  private String page = "/com/lab/fxml/basic/home.fxml";

  /**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Instanzia la classe a se stessa.
   * Ottiene le coordinate del mouse alla pressione sulla toolbar, e alla suo rilascio per riposizionare la finestra.
   * </p>
   */
  @FXML
  public void initialize()
  {

    toolbarController = this;

    toolbar.setOnMousePressed(event -> {
        offsetX = event.getSceneX();
        offsetY = event.getSceneY();
    });

    toolbar.setOnMouseDragged(event -> {
      Stage stage = (Stage) toolbar.getScene().getWindow();
      stage.setX(event.getScreenX() - offsetX);
      stage.setY(event.getScreenY() - offsetY);
    });
  }

  /**
   * Mostra il bottone Indietro della toolbar.
   * <p>
   * In base alla scelta passata come parametro mostra o meno il bottone Indietro.
   * </p>
   * 
   * @param show Valore booleano della scelta.
   */
  public static void showBackButton(boolean show)
  {
    if(toolbarController != null) {
      toolbarController.back_B.setVisible(show);
      toolbarController.back_B.setManaged(show);
    }
  }

  /**
   * Imposta la pagina di ritorno del bottone Indietro della toolbar.
   * <p>
   * In base alla scelta passata come parametro mostra o meno il bottone di ritorno indietro e imposta la pagina a cui tornare.
   * </p>
   * 
   * @param show Valore booleano della scelta.
   * @param prevPage Indirizzo della pagina a cui tornare.
   */
  public static void setupBackButton(boolean show, String prevPage)
  {
    if(toolbarController != null) {
      toolbarController.back_B.setVisible(show);
      toolbarController.back_B.setManaged(show);
      toolbarController.page = prevPage;
    }
  }

  /**
   * Imposta la visibilita' dei bottoni di accesso, registrazione, uscita dal profilo della toolbar.
   * <p>
   * In base alla scelta passata come parametro mostra o meno i bottoni specificati.
   * </p>
   * 
   * @param signin Valore booleano della scelta.
   * @param signup Valore booleano della scelta.
   * @param signout Valore booleano della scelta.
   */
  public static void showLeftSide(boolean signin, boolean signup, boolean signout)
  {
    if(toolbarController != null) {
      toolbarController.signin_B.setVisible(signin);
      toolbarController.signin_B.setManaged(signin);
      toolbarController.signup_B.setVisible(signup);
      toolbarController.signup_B.setManaged(signup);
      toolbarController.signout_B.setVisible(signout);
      toolbarController.signout_B.setManaged(signout);
    }
  }

  /**
   * Gestisce l'evento di ritorno alla pagina precedente.
   * <p>
   * Direzione l'utente alla pagina salvata come precedente dal metodo {@link #setupBackButton(boolean, String)}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Indietro.
   */
  @FXML
  public void backClicked(ActionEvent event)
  {
    PageController.selectPage(page);
  }

  /**
   * Gestisce l'evento di accesso alla piattaforma.
   * <p>
   * Direzione l'utente alla pagina di accesso alla piattaforma {@link SigninController}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Accedi.
   */
  @FXML
  public void signinClicked(ActionEvent event)
  {
    PageController.selectPage("/com/lab/fxml/access/signin.fxml");
  }

  /**
   * Gestisce l'evento di registrazione alla piattaforma.
   * <p>
   * Direzione l'utente alla pagina di registrazione alla piattaforma {@link SignupController}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Registrati.
   */
  @FXML
  public void signupClicked(ActionEvent event)
  {;
    PageController.selectPage("/com/lab/fxml/access/signup.fxml");
  }

  /**
   * Gestisce l'evento di uscita dal profilo.
   * <p>
   * Direzione l'utente alla pagina principali di scelta della metodologia di accesso alla piattaforma {@link HomeController}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Esci.
   */
  @FXML
  public void signoutClicked(ActionEvent event)
  {
    if(Session.getCurrentUser() != null){
      final int userId = Session.getCurrentUser().getId();

      CompletableFuture.runAsync(() -> {
        try {
          ServerConnection.getServer().signout(userId);
        } catch(Exception e) { }
      });
    }

    Session.signOut();
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Uscito");
    PageController.selectPage("/com/lab/fxml/basic/home.fxml");
  }

  /**
   * Gestisce l'evento di uscita dall'applicazione.
   * <p>
   * Chiude l'applicazione.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone X.
   */
  @FXML
  public void exitClicked(ActionEvent event)
  { 
    if(Session.getCurrentUser() != null){
      final int userId = Session.getCurrentUser().getId();

      CompletableFuture.runAsync(() -> {
        try {
          ServerConnection.getServer().signout(userId);
        } catch(Exception e) { }
      });
    }

    Session.signOut();
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Uscito");
    System.out.println("[" + StringColor.BLUE + "INFO" + StringColor.RESET + "] Applicazione chiusa");
    Platform.exit();
  }

  /**
   * Gestisce l'evento di minimizzazione dell'applicazione.
   * <p>
   * Minimizza la finestra mettendola a finestra.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone -.
   */
  @FXML
  public void minimizeClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.BLUE + "INFO" + StringColor.RESET + "] Applicazione in finestra");
    Stage stage = (Stage) toolbar.getScene().getWindow();
    stage.setIconified(true);
  }
}
