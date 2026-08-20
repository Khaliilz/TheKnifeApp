package com.lab.controller.user;

import java.io.IOException;

import com.lab.database.model.Restaurant;
import com.lab.database.model.Session;
import com.lab.utility.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DetailsUserController {

  @FXML private Text name_L;
  @FXML private Label address_L;
  @FXML private Label price_L;
  @FXML private Label delivery_L;
  @FXML private Label booking_L;
  @FXML private Label cuisine_L;
	@FXML private VBox listOfComments;
  @FXML private Button reviewButton;

  
  public void setDetails(Restaurant r)
	{
    name_L.setText(r.getName());

    String fullAddress = r.getAddress();
    String shortAddress = fullAddress;
    if(fullAddress.contains(",")){
      String[] split = fullAddress.split(",");
      if(split.length >= 2) shortAddress = split[0] + ", " + split[1]; 
    }
    address_L.setText(shortAddress);

    price_L.setText((r.getPrice() == null || r.getPrice().isEmpty()) ? "..." : r.getPrice());
    delivery_L.setText((r.getDelivery() == null || r.getDelivery().isEmpty()) ? "No" : r.getDelivery());
    booking_L.setText((r.getBooking() == null || r.getBooking().isEmpty()) ? "No" : r.getBooking());
    cuisine_L.setText(r.getCuisine());

		loadReviews();

    if(Session.getCurrentUser() == null) {
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
      {"Luca Verdi", "5", "Tutto perfetto, torneremo sicuramente."}
    };

    listOfComments.getChildren().clear();

    for(String[] r : mockReviews) {
      try{
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(com.lab.App.class.getResource("/com/lab/fxml/user/reviewsRow.fxml"));
        VBox row = loader.load();

        ReviewsRowController controller = loader.getController();
        controller.setReview(r);

        listOfComments.getChildren().add(row);
      }catch (IOException e) {
        System.out.println("[" + com.lab.utility.Lib.RED + "ERROR" + com.lab.utility.Lib.RESET + "] Loading review row");
        e.printStackTrace();
      }
    }
  }
}
