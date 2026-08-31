/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.user;

import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FilterController gestisce l'interfaccia relativa al popup dei filtri di ricerca.
 * <p>
 * Permette di ottenere dalle componenti grafiche, la metodologia di filtro da applicare alla ricerca.
 * </p> 
 */
public class FilterController {
  
  @FXML private TextField cuisine_TF;
  @FXML private ToggleGroup priceGroup;
  @FXML private ToggleGroup deliveryGroup;
  @FXML private ToggleGroup starsGroup;
  @FXML private ToggleGroup bookingGroup;

  /**
   * Gestisce l'evento di scelta dei filtri di ricerca.
   * <p>
   * Decodifica i filtri scelti dall'utente dalle componenti grafiche.
   * Le spedisce alla homepage dell'utente, la quale eseguira' la ricerca, chiamando il metodo {@link UserHomeController#applyFilters(String, String, String, String, String)}
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Indietro
   */
  @FXML
  public void applyFilterClicked(ActionEvent event)
  {
    String cuisine = cuisine_TF.getText();
    if(cuisine != null && cuisine.trim().isEmpty()) cuisine = null;

    String price = priceGroup.getSelectedToggle() != null ? ((RadioButton) priceGroup.getSelectedToggle()).getText() : null;
    String delivery = deliveryGroup.getSelectedToggle() != null ? ((RadioButton) deliveryGroup.getSelectedToggle()).getText() : null;
    String stars = starsGroup.getSelectedToggle() != null ? ((RadioButton) starsGroup.getSelectedToggle()).getText() : null;
    String booking = bookingGroup.getSelectedToggle() != null ? ((RadioButton) bookingGroup.getSelectedToggle()).getText() : null;

    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Filter scene closed");

    UserHomeController.getInstance().applyFilters(cuisine, price, delivery, booking, stars);

    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
  }
}
