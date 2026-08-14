package com.lab.controller;

import java.io.IOException;

import com.lab.App;
import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DetailsUserController {

  @FXML private Text name_L;
  @FXML private Label address_L;
  @FXML private Label stats_L;
	@FXML private VBox listOfComments;

  
  public void setDetails(String[] r)
	{
    name_L.setText(r[0]);
    address_L.setText(r[1]);
		caricaRecensioni();
  }

  @FXML
  public void backClicked(ActionEvent event)
	{
    System.out.println("[" + Lib.GREEN + "ACTION] " + Lib.RESET + "Details view closed");
    UserHomeController.getInstance().closeDetails();
  }

	@FXML
  public void reviewClicked(ActionEvent event)
	{
    System.out.println("[" + Lib.GREEN + "ACTION] " + Lib.RESET + "Review clicked");

    String name = name_L.getText();
    UserHomeController.getInstance().openWriteComment(name);
  }

  private void caricaRecensioni()
  {
    listOfComments.getChildren().clear();

    String[][] mockReviews = {
      {"Mario Rossi", "5", "Posto fantastico, la carne era cotta alla perfezione. Personale super gentile e accogliente. Consigliatissimo!"},
      {"Giulia Bianchi", "4", "Molto buono ma il dolce non mi ha convinto del tutto. Comunque il servizio è stato velocissimo."},
      {"Luca Verdi", "5", "Tutto perfetto, torneremo sicuramente."}
    };

    for(String[] rev : mockReviews){
      VBox commentBox = new VBox();
      commentBox.setSpacing(5);
      commentBox.setStyle("-fx-border-color: #009900; -fx-border-width: 0 0 1 0; -fx-padding: 10 0 15 0;");

      Label author_L = new Label(rev[0] + " \t ⭐ " + rev[1] + "/5");
      author_L.getStyleClass().add("titleLabel");
      author_L.setStyle("-fx-font-size: 14px;");

      Label text_L = new Label(rev[2]);
      text_L.getStyleClass().add("textLabel");
      text_L.setStyle("-fx-font-size: 14px;");
      
      text_L.setWrapText(true); 

      commentBox.getChildren().addAll(author_L, text_L);

      listOfComments.getChildren().add(commentBox);
    }
  }
}
