package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class RestaurantReviewsRowController {

  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text comment;

  private String[] review;

  public void setReview(String[] r, String[] c)
  {
    review = c;
    name.setText(r[0]);
    address.setText(r[1]);
    starsNum.setText(c[3]);
    comment.setText(c[1]);
  }

  @FXML
  public void viewClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] View button clicked");
    UserHomeController.getInstance().viewComment(review);
  }
}
