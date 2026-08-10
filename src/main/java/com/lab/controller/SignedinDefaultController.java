package com.lab.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.lab.App;
import com.lab.Lib;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SignedinDefaultController {
  
  @FXML private StackPane rightMenuArea;
  @FXML private Text titleText;
  @FXML private VBox listOfRestaurants_VB;
  @FXML private ScrollPane listContainer_SP;
  @FXML private StackPane leftMenuArea;

  private static SignedinDefaultController instance;
  private javafx.scene.Node detailsNode;
  private javafx.scene.Node commentNode;

  @FXML
  public void initialize()
  {
    instance = this;

    PageController.showTitle(false); 
    ToolbarController.showBackButton(false);
    loadRightMenu("Ristoranti nelle vicinanze", "rightMenuSearch.fxml");
    
    loadNearest();
  }

  public static SignedinDefaultController getInstance()
  {
    return instance;
  }

  public void loadNearest()
  {
    titleText.setText("Ristoranti nelle vicinanze");
    listOfRestaurants_VB.getChildren().clear();

    fillRestaurants();
  }

  public void loadBookmarked()
  {
    closeDetails();
    closeComment();

    titleText.setText("Ristoranti preferiti");
    listOfRestaurants_VB.getChildren().clear();

    fillRestaurants();
  }

  public void loadReviewed()
  {
    closeDetails();
    closeComment();

    titleText.setText("Ristoranti recensiti");
    listOfRestaurants_VB.getChildren().clear();

    fillReviewed();
  }

  public void loadRightMenu(String newTitle, String fileName)
  {
    try{
      titleText.setText(newTitle);
      Parent selectedMenu = FXMLLoader.load(App.class.getResource(fileName));
      rightMenuArea.getChildren().setAll(selectedMenu);
    }catch(IOException e) {
      System.out.print("[" + Lib.RED + "ERROR" + Lib.RESET + "] fail to load the right menu: " + fileName);
      e.printStackTrace();
    }
  }

  public void searchByPlace(String place)
  {
    closeDetails();
    closeComment();

    titleText.setText("Ristoranti a " + place);
    listOfRestaurants_VB.getChildren().clear();

    fillRestaurants();
  }

  public void applyFilters()
  {
    closeDetails();
    closeComment();
    
    titleText.setText("Ristoranti trovati");
    listOfRestaurants_VB.getChildren().clear();
    
    fillRestaurants(); 
  }

  private void fillRestaurants() 
  {
    String[] ristorante1 = {"Ristorante 1", "via Trieste 12, Milano", "5", "10"};
    ArrayList<String[]> ristoranti = new ArrayList<>();
    for(int i=0; i<15; i++) ristoranti.add(ristorante1);

    for(String[] r : ristoranti){
      HBox line_HB = new HBox();
      line_HB.setSpacing(15);
      line_HB.setAlignment(Pos.CENTER_LEFT);
      line_HB.setStyle("-fx-border-color: #009900; -fx-border-width: 0 0 1 0; -fx-padding: 20;");

      VBox text_VB = new VBox();
      text_VB.setSpacing(5);

      Label name_L = new Label(r[0] + "\t-\t" + r[1]);
      name_L.getStyleClass().add("theknifeLabel");
      Label stats_L = new Label("⭐ " + r[2] + " Stelle \t 💬 " + r[3] + " Recensioni");

      text_VB.getChildren().addAll(name_L, stats_L);

      Pane spacer = new Pane();
      HBox.setHgrow(spacer, Priority.ALWAYS);

      Button details_B = new Button("Dettagli");
      details_B.getStyleClass().add("detailButton");
      details_B.setPrefSize(150, 40);
      details_B.setOnAction(e -> {
        System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Detail button pressed");
        viewDetails(r);
      });

      final boolean[] isBookmarked = {false};
      Button bookmark_B = new Button();
      bookmark_B.getStyleClass().add("bookmarkButton");
      bookmark_B.setPrefSize(40, 40);
      bookmark_B.setMinSize(40, 40);
      bookmark_B.setOnAction(e -> {
        isBookmarked[0] = !isBookmarked[0]; 
          
        if(isBookmarked[0]){
          bookmark_B.setStyle("-fx-background-image: url('/com/lab/img/bookmarkedIcon.png');");
          System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Bookmarked [" + r[0] + "]");
        }else{
          bookmark_B.setStyle("-fx-background-image: url('/com/lab/img/bookmarkIcon.png');");
          System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Unbookmarked [" + r[0] + "]");
        }
      });

      line_HB.getChildren().addAll(text_VB, spacer, details_B, bookmark_B);
      listOfRestaurants_VB.getChildren().addAll(line_HB);
    }
  }

  private void fillReviewed()
  {
    String[] ristorante1 = {"Ristorante 1", "via Trieste 12, Milano", "5", "10"};
    ArrayList<String[]> ristoranti = new ArrayList<>();
    for(int i=0; i<15; i++) ristoranti.add(ristorante1);

    for(String[] r : ristoranti){
      HBox line_HB = new HBox();
      line_HB.setSpacing(15);
      line_HB.setAlignment(Pos.CENTER_LEFT);
      line_HB.setStyle("-fx-border-color: #009900; -fx-border-width: 0 0 1 0; -fx-padding: 20;");

      VBox text_VB = new VBox();
      text_VB.setSpacing(5);

      Label name_L = new Label(r[0] + "\t-\t" + r[1]);
      name_L.getStyleClass().add("theknifeLabel");
      Label stats_L = new Label("Il tuo voto: ⭐⭐⭐⭐⭐ \t 💬 Il cibo era fantastico, il cameriere molto gentile. Ci tornerò sicuramente la prossima settimana con gli amici!");

      text_VB.getChildren().addAll(name_L, stats_L);

      Pane spacer = new Pane();
      HBox.setHgrow(spacer, Priority.ALWAYS);

      Button view_B = new Button("Visualizza");
      view_B.getStyleClass().add("detailButton");
      view_B.setPrefSize(150, 40);
      view_B.setOnAction(e -> {
        System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] View button pressed");
        String[] comment = {"Ristorante 1", "Ottimo!", "Grazie!", "3"};
        viewComment(comment);
      });

      line_HB.getChildren().addAll(text_VB, spacer, view_B);
      listOfRestaurants_VB.getChildren().addAll(line_HB);
    }
  }

  public void viewDetails(String[] restaurant)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/details.fxml"));
      detailsNode = loader.load();

      DetailsController controller = loader.getController();
      controller.setDetails(restaurant);

      listContainer_SP.setVisible(false); 
      leftMenuArea.getChildren().add(detailsNode); 

    }catch(IOException e) {
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading details");
      e.printStackTrace();
    }
  }

  public void closeDetails()
  {
    if(detailsNode != null){
      leftMenuArea.getChildren().remove(detailsNode);
      detailsNode = null;
    }
    
    listContainer_SP.setVisible(true); 
  }

  public void viewComment(String[] comment)
  {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/viewComment.fxml"));
      commentNode = loader.load();

      ViewCommentController controller = loader.getController();
      controller.setComment(comment);

      listContainer_SP.setVisible(false); 
      leftMenuArea.getChildren().add(commentNode); 

    }catch(IOException e) {
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Loading comment");
      e.printStackTrace();
    }
  }

  public void closeComment()
  {
    if(commentNode != null){
      leftMenuArea.getChildren().remove(commentNode);
      commentNode = null;
    }
    
    listContainer_SP.setVisible(true); 
  }
}