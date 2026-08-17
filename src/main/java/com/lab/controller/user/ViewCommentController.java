package com.lab.controller.user;

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
  @FXML private RadioButton starsOne;
  @FXML private RadioButton starsTwo;
  @FXML private RadioButton starsThree;
  @FXML private RadioButton starsFour;

  @FXML
  public void saveClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION] " + Lib.RESET + "Save clicked");
    UserHomeController.getInstance().closeComment();
    UserHomeController.getInstance().loadReviews();
  }

  @FXML void removeClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION] " + Lib.RESET + "Remove clicked");
    UserHomeController.getInstance().closeComment();
    UserHomeController.getInstance().loadReviews();
  }
  
  public void setComment(String[] c)
  {
    name_R.setText(c[0]);
    comment.setText(c[1]);
    answer.setText(c[2]);

    int stars = Integer.parseInt(c[3]);

    switch(stars){
      case 1:
        starsOne.setSelected(true);
        break;
      case 2:
        starsTwo.setSelected(true);
        break;
      case 3:
        starsThree.setSelected(true);
        break;
      case 4: 
        starsFour.setSelected(true);
        break;
      default:
        starsOne.setSelected(true);
    }
  }
}
