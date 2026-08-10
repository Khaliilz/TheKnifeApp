package com.lab.controller;

import java.io.IOException;

import com.lab.Lib;
import com.lab.App;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class PageController {
  
  @FXML private StackPane contentArea;
  @FXML private StackPane pageTitle;
  @FXML private Text textTitle;

  private static PageController instance;

  @FXML
  public void initialize()
  {
    instance = this;
    
    selectPage("home.fxml");
  }

  public static void showTitle(boolean show) {
      if (instance != null && instance.pageTitle != null) {
          instance.pageTitle.setVisible(show);
      }
  }

  public static void setTitleText(String text) {
      if (instance != null && instance.textTitle != null) {
          instance.textTitle.setText(text);
      }
  }
  
  public static void selectPage(String fileName)
  {
    if(instance != null) instance.loadPage(fileName);
    else System.out.print("[" + Lib.RED + "ERROR" + Lib.RESET + "]" + " File non trovato: " + fileName);
  }

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
    }catch(IOException e){
      System.out.print("[" + Lib.RED + "ERROR" + Lib.RESET + "]" + " Errore nel caricamento della pagina: " + fileName);
      e.printStackTrace();
    }
  }
}
