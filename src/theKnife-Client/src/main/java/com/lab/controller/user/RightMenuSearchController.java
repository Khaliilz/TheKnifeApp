/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.user;

import java.io.IOException;

import com.lab.model.Session;
import com.lab.utility.StringColor;
import com.lab.utility.ErrorContainer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * RightMenuSearchController gestisce l'interfaccia relativa al menu laterale e delle interazione con i suoi componenti grafici.
 */
public class RightMenuSearchController {
  
  @FXML private TextField luogo_TF;
  @FXML private Button bookmarkedButton;
  @FXML private Button reviewedButton;

  /**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Reimposta la corretta visualizzazione dello stile degli input dell'utente.
	 * Imposta la pressione del tasto invio all'evento {@link #searchClicked(ActionEvent)}
   * Imposta la visualizzazione dei bottoni "recensiti" e "preferiti" visibili solo all'utente registrato.
   * </p>
   */
  @FXML
  public void initialize()
  {
    luogo_TF.setOnAction(this::searchClicked);
    ErrorContainer.resetBorder(luogo_TF);

    if(Session.getCurrentUser() == null) {
      bookmarkedButton.setVisible(false);
      bookmarkedButton.setManaged(false);
      reviewedButton.setVisible(false);
      reviewedButton.setManaged(false);
    }
  }

  /**
   * Gestisce l'evento di ricerca dei ristoranti.
   * <p>
   * Controlla la validita' degli input dell'utente.
   * Chiama il metodo {@link UserHomeController#searchByPlace(String)} che si occupa di applicare la ricerca.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Cerca.
   */
  @FXML
  public void searchClicked(ActionEvent event)
  {
    boolean error = false;
    String place = luogo_TF.getText();
    
    if(place.isEmpty()) {
      ErrorContainer.errorBorder(luogo_TF);
      error = true;
    }

    if(error) return;

    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Search: " + place);
    UserHomeController.getInstance().searchByPlace(place);
  }

  /**
   * Gestisce l'evento di richiesta di selezione dei filtri.
   * <p>
   * Ottiene la risorsa del popup dei filtri tramite indirizzo.
   * Ne configura la finestra e la mostra.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Filtra.
   */
  @FXML
  public void filterClicked(ActionEvent event)
  {
    try{
      Parent filterRoot = FXMLLoader.load(getClass().getResource("/com/lab/fxml/user/filter.fxml"));
      
      Stage popupStage = new Stage();
      
      popupStage.setTitle("TheKnife - Filter");
      Image icon = new Image(getClass().getResource("/com/lab/img/logo.png").toExternalForm());
		  popupStage.getIcons().add(icon);
      popupStage.initStyle(StageStyle.TRANSPARENT); 
      popupStage.setResizable(false);
      popupStage.initModality(Modality.APPLICATION_MODAL); 
      
      Scene scene = new Scene(filterRoot, 450, 530);
      scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
      popupStage.setScene(scene);
      popupStage.show();
      System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Filter button clicked");
    }catch(IOException e) {
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Filter page loading");
      e.printStackTrace();
    }
  }

  /**
   * Gestisce l'evento di richiesta di visualizzazione dei ristoranti preferiti.
   * <p>
   * Chiama il metodo {@link UserHomeController#loadBookmarked()} che si occupa di visualizzare i ristoranti preferiti all'utente.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Preferiti.
   */
  @FXML
  public void bookmarkClicked(ActionEvent event)
  {
    UserHomeController.getInstance().loadBookmarked();
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Bookmark button clicked");
  }

  /**
   * Gestisce l'evento di richiesta di visualizzazione dei ristoranti recensiti.
   * <p>
   * Chiama il metodo {@link UserHomeController#loadReviews()} che si occupa di visualizzare i ristoranti recensiti all'utente.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Recensiti.
   */
  @FXML
  public void reviewClicked(ActionEvent event)
  {
    UserHomeController.getInstance().loadReviews();
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Review button clicked");
  }
}