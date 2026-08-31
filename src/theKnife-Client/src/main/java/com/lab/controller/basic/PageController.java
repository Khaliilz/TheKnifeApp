/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.controller.basic;

import java.io.IOException;

import com.lab.App;
import com.lab.utility.StringColor;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * PageController Gestisce l'interfaccia scheletro della piattaforma.
 * <p>
 * Questa classe si occupa di selezionare e caricare le schermate corrette.
 * </p>
 */
public class PageController {
  
  @FXML private StackPane contentArea;
  @FXML private StackPane pageTitle;
  @FXML private Text welcomeText;

  private static PageController instance;

  /**
   * initialize e' un metodo invocato automaticamente da JavaFX al caricamento del file fxml.
   * <p>
   * Instanzia la classe a se stessa.
   * Carica la pagina principale di accesso al sistema {@link HomeController}
   * </p>
   */
  @FXML
  public void initialize()
  {
    instance = this;
    
    selectPage("/com/lab/fxml/basic/home.fxml");
  }

  /**
   * Mostra il titolo della pagina principale.
   * <p>
   * In base alla scelta passata come parametro mostra o meno il titolo della pagina principale.
   * </p>
   * 
   * @param show Valore booleano della scelta.
   */
  public static void showTitle(boolean show) {
      if (instance != null && instance.pageTitle != null) {
          instance.pageTitle.setVisible(show);
      }
  }

  /**
   * Modifica il contenuto del titolo della pagina principale.
   * <p>
   * Modifica il titolo della pagina principale con il parametro passato come argomento.
   * </p>
   * 
   * @param text Valore del titolo della pagina.
   */
  public static void setTitleText(String text) {
      if (instance != null && instance.welcomeText != null) {
          instance.welcomeText.setText(text);
      }
  }
  
  /**
   * Seleziona la schermata da caricare.
   * <p>
   * Seleziona la schermata passata come parametro, e lo mostra caricandolo tramite il metodo {@link #loadPage(String)}.
   * </p>
   * 
   * @param fileName indirizzo della schermata da caricare.
   */
  public static void selectPage(String fileName)
  {
    if(instance != null) instance.loadPage(fileName);
    else System.out.print("[" + StringColor.RED + "ERROR" + StringColor.RESET + "]" + " File not found: " + fileName);
  }

  /**
   * Carica la schermata.
   * <p>
   * Ottiene la pagina come risorsa, e la carica applicando una transizione fluida.
   * </p>
   * 
   * @param fileName indirizzo della schermata da caricare.
   */
  public void loadPage(String fileName)
  {
    try{
      Parent selectedPage = FXMLLoader.load(App.class.getResource(fileName));
      selectedPage.setOpacity(0);
      contentArea.getChildren().setAll(selectedPage);

      FadeTransition fadeIn = new FadeTransition(Duration.millis(300), selectedPage);
      fadeIn.setToValue(1.0);
      fadeIn.setOnFinished(event -> {
        if (contentArea.getChildren().size() > 1) {
            contentArea.getChildren().remove(0, contentArea.getChildren().size() - 1);
        }
      });
      fadeIn.play();
    }catch(IOException e) {
      System.out.print("[" + StringColor.RED + "ERROR" + StringColor.RESET + "]" + " Loading page: " + fileName);
      e.printStackTrace();
    }
  }
}
