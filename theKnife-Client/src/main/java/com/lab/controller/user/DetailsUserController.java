package com.lab.controller.user;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import com.lab.model.Restaurant;
import com.lab.model.Session;
import com.lab.server.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DetailsUserController {

  @FXML private Text name_L;
  @FXML private Label address_L;
  @FXML private Label price_L;
  @FXML private Label delivery_L;
  @FXML private Label booking_L;
  @FXML private Label cuisine_L;
  @FXML private ScrollPane listContainer;
	@FXML private VBox listOfComments;
  @FXML private Button reviewButton;
  @FXML private Label emptyLabel;

  private Restaurant restaurant;
  
  public void setDetails(Restaurant r)
	{
    restaurant = r;

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

		loadReviews(r.getId());

    if(Session.getCurrentUser() == null) {
      reviewButton.setVisible(false);
      reviewButton.setManaged(false);
    }
  }

  @FXML
  public void backClicked(ActionEvent event)
	{
    System.out.println("[" + StringColor.GREEN + "ACTION] " + StringColor.RESET + "Details view closed");
    UserHomeController.getInstance().closeDetails();
  }

	@FXML
  public void reviewClicked(ActionEvent event)
	{
    System.out.println("[" + StringColor.GREEN + "ACTION] " + StringColor.RESET + "Review clicked");

    String name = name_L.getText();
    UserHomeController.getInstance().openWriteComment(restaurant);
  }

  private void loadReviews(int restaurantId)
  {
    listOfComments.getChildren().clear();

    try{
      List<String[]> reviews = ServerConnection.getServer().getRestaurantReviews(restaurantId);
      
      boolean isEmpty = reviews.isEmpty();
      emptyLabel.setVisible(isEmpty);
      emptyLabel.setManaged(isEmpty);
      listContainer.setVisible(!isEmpty);
      if(isEmpty) return;

      for(String[] r : reviews) {
        try{
          FXMLLoader loader = new FXMLLoader(com.lab.App.class.getResource("/com/lab/fxml/user/reviewsRow.fxml"));
          VBox row = loader.load();

          ReviewsRowController controller = loader.getController();
          controller.setReview(r);

          listOfComments.getChildren().add(row);
        }catch (IOException e) {
          System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Loading review row");
          e.printStackTrace();
        }
      }
    } catch (RemoteException e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Server comunication");
    }
  }
}
