package com.lab.controller;

import java.io.IOException;

import com.lab.App;
import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DetailsUserController {

  @FXML private Text name_L;
  @FXML private Label address_L;
  @FXML private Label stats_L;
	@FXML private VBox listOfComments;
  @FXML private Button reviewButton;

  
  public void setDetails(String[] r)
	{
    name_L.setText(r[0]);
    address_L.setText(r[1]);
		loadReviews();

    if(UserHomeController.isGuest){
      reviewButton.setVisible(false);
      reviewButton.setManaged(false);
    }
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

  private void loadReviews()
  {
    listOfComments.getChildren().clear();

    String[][] mockReviews = {
      {"Mario Rossi", "5", "Posto fantastico, la carne era cotta alla perfezione. Personale super gentile e accogliente. Consigliatissimo!"},
      {"Giulia Bianchi", "4", "Molto buono ma il dolce non mi ha convinto del tutto. Comunque il servizio è stato velocissimo."},
      {"Luca Verdi", "5", "Tutto perfetto, torneremo sicuramente."},
      {"Mario Rossi", "5", "Posto fantastico, la carne era cotta alla perfezione. Personale super gentile e accogliente. Consigliatissimo!"},
      {"Giulia Bianchi", "4", "Molto buono ma il dolce non mi ha convinto del tutto. Comunque il servizio è stato velocissimo."},
      {"Luca Verdi", "5", "Tutto perfetto, torneremo sicuramente."},
      {"Mario Rossi", "5", "Posto fantastico, la carne era cotta alla perfezione. Personale super gentile e accogliente. Consigliatissimo!"},
      {"Giulia Bianchi", "4", "Molto buono ma il dolce non mi ha convinto del tutto. Comunque il servizio è stato velocissimo."},
      {"Luca Verdi", "5", "Tutto perfetto, torneremo sicuramente."}
    };

    listOfComments.getChildren().clear();

    for(String[] r : mockReviews){
      try{
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(com.lab.App.class.getResource("/com/lab/reviewsRow.fxml"));
        VBox row = loader.load();

        ReviewsRowController controller = loader.getController();
        controller.setReview(r);

        listOfComments.getChildren().add(row);
      }catch (IOException e){
        System.out.println("[" + com.lab.Lib.RED + "ERROR" + com.lab.Lib.RESET + "] Loading review row");
        e.printStackTrace();
      }
    }
  }
}
