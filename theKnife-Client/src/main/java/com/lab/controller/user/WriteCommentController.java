package com.lab.controller.user;

import java.rmi.RemoteException;

import com.lab.model.Restaurant;
import com.lab.model.Session;
import com.lab.server.ServerConnection;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class WriteCommentController {
  @FXML private Text name_R;
  @FXML private TextArea comment;
  @FXML private ToggleGroup starsGroup;
  @FXML private RadioButton starsOne;
  @FXML private RadioButton starsTwo;
  @FXML private RadioButton starsThree;

  private Restaurant currentRestaurant;
  private boolean exists = false;

  public void setRestaurantReview(Restaurant restaurant)
  {
    currentRestaurant = restaurant;
    name_R.setText(restaurant.getName());

    int userId = Session.getCurrentUser().getId();
    int restaurantId = currentRestaurant.getId();

    String[] existingReview = null;
    try {
      existingReview = ServerConnection.getServer().getUserReview(userId, restaurantId);
    }catch(RemoteException e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Server comunication");
    }

    if(existingReview != null) {
      exists = true;
      comment.setText(existingReview[1] != null ? existingReview[1] : "");
      int userStar = Integer.parseInt(existingReview[0]);
      if(userStar == 1) starsOne.setSelected(true);
      else if(userStar == 2) starsTwo.setSelected(true);
      else starsThree.setSelected(true);
    }
  }

  @FXML void backClicked(ActionEvent e)
  {
    System.out.println("[" + StringColor.GREEN + "ACTION" + StringColor.RESET + "] Review view closed");
    UserHomeController.getInstance().closeWriteComment();
  }

  @FXML
  public void saveClicked(ActionEvent e)
  {
    RadioButton selectedRadioButton = (RadioButton) starsGroup.getSelectedToggle();
    int stars = Integer.parseInt(selectedRadioButton.getText());
    String text = comment.getText();

    int userId = Session.getCurrentUser().getId();
    int restaurantId = currentRestaurant.getId();
    boolean reviewed = false;
    
    if(exists) {
      try{
        reviewed = ServerConnection.getServer().updateReview(userId, restaurantId, stars, text);
        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Review updated");
      } catch (RemoteException ex) {
        ex.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Server comunication");
      }
    } else {
      try{
        reviewed = ServerConnection.getServer().addReview(userId, restaurantId, stars, text);
        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Review saved");
      } catch (RemoteException ex) {
        ex.printStackTrace();
        System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Server comunication");
      }
    }

    if(!reviewed) System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Failed saving review");
    UserHomeController.getInstance().closeWriteComment();
  }
}
