package com.lab.controller.restaurateur;

import com.lab.utility.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AnswerCommentController {
  
  @FXML private Text name;
  @FXML private TextArea comment;
  @FXML private TextArea answer;
  
  private String[] reviewData;

  public void setReviewData(String[] data)
  {
    reviewData = data;
    
    name.setText(data[0]);
    comment.setText(data[1]);
    comment.setEditable(false);
    comment.setFocusTraversable(false);
    answer.setText(data[3]);
    answer.requestFocus();
    answer.positionCaret(answer.getText().length());
  }

  @FXML
  public void saveClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Save button clicked");

    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
    stage.close();
  }
}
