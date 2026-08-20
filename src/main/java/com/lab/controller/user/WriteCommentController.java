package com.lab.controller.user;

import com.lab.database.model.Restaurant;
import com.lab.database.model.Session;
import com.lab.database.query.ReviewQ;
import com.lab.utility.Lib;

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
    String[] existingReview = ReviewQ.getUserReview(userId, restaurantId);
    if(existingReview != null) {
      exists = true;
      comment.setText(existingReview[1] != null ? existingReview[1] : "");
      int userStar = Integer.parseInt(existingReview[0]);
      if(userStar == 1) starsOne.setSelected(true);
      else if(userStar == 2) starsTwo.setSelected(true);
      else starsThree.setSelected(true);
    }
  }

  @FXML
  public void saveClicked(ActionEvent e)
  {
    RadioButton selectedRadioButton = (RadioButton) starsGroup.getSelectedToggle();
    int stars = Integer.parseInt(selectedRadioButton.getText());
    String text = comment.getText();

    int userId = Session.getCurrentUser().getId();
    int restaurantId = currentRestaurant.getId();
    boolean reviewed;
    
    if(exists) {
      reviewed = ReviewQ.updateReview(userId, restaurantId, stars, text);
      System.out.println("[" + Lib.PURPLE + "DATABASE" + Lib.RESET + "] Review updated");
    } else {
      reviewed = ReviewQ.addReview(userId, restaurantId, stars, text);
      System.out.println("[" + Lib.PURPLE + "DATABASE" + Lib.RESET + "] Review saved");
    }

    if(!reviewed) System.out.println("[" + Lib.PURPLE + "DATABASE" + Lib.RESET + "] Failed saving review");

    UserHomeController.getInstance().closeWriteComment();
  }
}
