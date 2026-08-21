package com.lab.controller.user;

import com.lab.utility.StringColor;
import com.lab.utility.ErrorContainer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class RestaurantReviewsRowController {

  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text comment;

  private String restaurantName;
  private String restaurantId;
  private String[] review;

  public void setReview(String[] r, String[] c)
  {
    restaurantName = r[0];
    restaurantId = r[4];
    review = c;
    name.setText(r[0]);
    String fullAddress = r[1];
    String shortAddress = fullAddress;
    if(fullAddress.contains(",")){
      String[] split = fullAddress.split(",");
      if(split.length >= 2) shortAddress = split[0] + ", " + split[1]; 
    }
    address.setText(shortAddress);
    starsNum.setText(c[0]);
    comment.setText(c[1]);
  }

  @FXML
  public void viewClicked(ActionEvent e)
  {
    System.out.println("[" + StringColor.GREEN + "ACTION" + StringColor.RESET + "] View button clicked");
    String[] completedReview = {restaurantName, review[1], review[2], review[0], restaurantId};
    UserHomeController.getInstance().viewComment(completedReview);
  }
}
