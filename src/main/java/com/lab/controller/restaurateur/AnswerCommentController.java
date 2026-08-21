package com.lab.controller.restaurateur;

import java.rmi.RemoteException;

import com.lab.server.ServerConnection;
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

    comment.setText(data[2]);
    comment.setEditable(false);
    comment.setFocusTraversable(false);

    if(data[3] != null) answer.setText(data[3]);
    answer.requestFocus();
    answer.positionCaret(answer.getText().length());
  }

  @FXML
  public void saveClicked(ActionEvent e)
  {
    int userId = Integer.parseInt(reviewData[4]);
    int restaurantId = Integer.parseInt(reviewData[5]);
    String answerText = answer.getText().trim();

    try{
      boolean success = ServerConnection.getServer().saveReviewAnswer(userId, restaurantId, answerText);
      
      if(success) System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Answer saved");
      else System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Failed to save answer");

      Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
      stage.close();
    } catch (RemoteException ex) {
      ex.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
  }
}
