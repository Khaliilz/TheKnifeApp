package com.lab.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ReviewsRowController {

  @FXML private Text name;
  @FXML private Text starsNum;
  @FXML private Label comment;
  @FXML private Label answerL;
  @FXML private Label answer;

  public void setReview(String[] c) 
  {
    name.setText(c[0]);
    starsNum.setText(c[1]);
    comment.setText(c[2]);
    
    if(c.length > 3 && c[3] != null && !c[3].isEmpty()) {
      answer.setText(c[3]);
      answerL.setVisible(true);
      answerL.setManaged(true);
      answer.setVisible(true);
      answer.setManaged(true);
    } else {
      answerL.setVisible(false);
      answerL.setManaged(false);
      answer.setVisible(false);
      answer.setManaged(false);
    }
  }
}
