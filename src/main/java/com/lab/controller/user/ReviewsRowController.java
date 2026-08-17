package com.lab.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ReviewsRowController {
  @FXML private Text name;
  @FXML private Text starsNum;
  @FXML private Label comment;

  public void setReview(String[] c) 
  {
    name.setText(c[0]);
    starsNum.setText(c[1]);
    comment.setText(c[2]);
  }
}
