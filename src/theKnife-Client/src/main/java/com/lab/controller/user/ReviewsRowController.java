/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 * ReviewsRowController gestisce l'interfaccia relativa alla singola riga della lista di recensioni di un ristorante, nella visualizzazione dei dettagli del ristorante per l'utente.
 */
public class ReviewsRowController {

  @FXML private Text name;
  @FXML private Text starsNum;
  @FXML private Label comment;
  @FXML private Label answerL;
  @FXML private Label answer;

  /**
   * Imposta il valore dei componenti grafici delle informazioni della singola recensione.
   * <p>
   * Dalla recensione ottenuta dalla lista degli argomenti, ne prelega le informazioni e aggiorna la grafica testuale delle relative informazioni.
   * Inoltre se il ristoratore ha risposto al commento, visualizza la sua risposta in coda al recensione dell'utente specifico. 
   * </p>
   * 
   * @param c La recensione da cui reperire i dati aggiuntivi
   */
  public void setReview(String[] c) 
  {
    name.setText(c[0]);
    starsNum.setText(c[1]);
    comment.setText(c[2]);
    
    if(c.length > 3 && c[3] != null && !c[3].isEmpty()) {
      answer.setText(c[3]);
      answerL.setVisible(true);
      answerL.setManaged(true);
      answer.setVisible(true);
      answer.setManaged(true);
    } else {
      answerL.setVisible(false);
      answerL.setManaged(false);
      answer.setVisible(false);
      answer.setManaged(false);
    }
  }
}
