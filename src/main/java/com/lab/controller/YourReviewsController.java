package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class YourReviewsController {
  
  @FXML private Text name;
  @FXML private Label comment;
  @FXML private Text starsNum;

  public void setReviewData(String[] c)
  {
    name.setText(c[0]);
    comment.setText(c[1]);
    starsNum.setText(c[2]);
  }

  @FXML public void answerClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Answer button pressed");
  }
}
