package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class ViewCommentController {

  @FXML private Text name_R;
  @FXML private TextArea comment;
  @FXML private Text answer;
  @FXML private RadioButton starsOne_RB;
  @FXML private RadioButton starsTwo_RB;
  @FXML private RadioButton starsThree_RB;
  @FXML private RadioButton starsFour_RB;

  @FXML
  public void saveClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION] " + Lib.RESET + "Save clicked");
    UserHomeController.getInstance().closeComment();
    UserHomeController.getInstance().loadReviewed();
  }

  @FXML void removeClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION] " + Lib.RESET + "Remove clicked");
    UserHomeController.getInstance().closeComment();
    UserHomeController.getInstance().loadReviewed();
  }
  
  public void setComment(String[] c)
  {
    name_R.setText(c[0]);
    comment.setText(c[1]);
    answer.setText(c[2]);

    int stars = Integer.parseInt(c[3]);

    switch(stars){
      case 1:
        starsOne_RB.setSelected(true);
        break;
      case 2:
        starsTwo_RB.setSelected(true);
        break;
      case 3:
        starsThree_RB.setSelected(true);
        break;
      case 4: 
        starsFour_RB.setSelected(true);
        break;
      default:
        starsOne_RB.setSelected(true);
    }
  }
}
