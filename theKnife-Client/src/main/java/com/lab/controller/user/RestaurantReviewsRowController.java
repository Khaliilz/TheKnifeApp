/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.user;

import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 * RestaurantReviewRowController gestisce l'interfaccia relativa alla singola riga dei ristoranti recensiti dall'utente.
 * <p>
 * Permette di visualizzare i ristoranti recensiti dall'utente.
 * </p> 
 */
public class RestaurantReviewsRowController {

  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text comment;

  private String restaurantName;
  private String restaurantId;
  private String[] review;

  /**
   * Imposta il valore dei componenti grafici delle informazioni della recensione.
   * <p>
   * Dai parametri, vengono estratti mostrati il nome del ristorante recensito, il suo indirizzo, le stelle assegnate dall'utente e il suo commento
   * </p>
   * 
   * @param r Il ristorante da cui reperire i dati aggiuntivi
   * @param c La recensione da cui reperire i dati aggiuntivi
   */
  public void setReview(String[] r, String[] c)
  {
    restaurantName = r[0];
    restaurantId = r[4];
    review = c;
    name.setText(r[0]);
    String fullAddress = r[1];
    String shortAddress = fullAddress;
    if(fullAddress.contains(",")){
      String[] split = fullAddress.split(",");
      if(split.length >= 2) shortAddress = split[0] + ", " + split[1]; 
    }
    address.setText(shortAddress);
    starsNum.setText(c[0]);
    comment.setText(c[1]);
  }

  /**
   * Gestisce l'evento di visualizzazione della recensione scritta dall'utente.
   * <p>
   * Chiama il metodo {@link UserHomeController#viewComment()} che si occupa di mostrare meglio la recensione e di modificarla se l'utente lo preferisce.
   * </p>
   * 
   * @param event L'evento scatenato dal click sul bottone Visualizza
   */
  @FXML
  public void viewClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] View button clicked");
    String[] completedReview = {restaurantName, review[1], review[2], review[0], restaurantId};
    UserHomeController.getInstance().viewComment(completedReview);
  }
}
