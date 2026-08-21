package com.lab.controller.user;

import java.rmi.RemoteException;

import com.lab.database.model.Session;
import com.lab.server.ServerConnection;
import com.lab.utility.Lib;

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

  private int currentRestaurantId;

  @FXML
  public void saveClicked(ActionEvent e)
  {
    int stars = 1;
    if(starsTwo.isSelected()) stars = 2;
    else if(starsThree.isSelected()) stars = 3;

    try{
      int userId = Session.getCurrentUser().getId();
      boolean success = ServerConnection.getServer().updateReview(userId, currentRestaurantId, stars, comment.getText());
      
      if(success) System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "Review updated successfully");
      else System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "Failed to update review.");

      UserHomeController.getInstance().closeComment();
      UserHomeController.getInstance().loadReviews();
    } catch (RemoteException ex) {
      ex.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
  }

  @FXML void removeClicked(ActionEvent e)
  {
    try{
      int userId = Session.getCurrentUser().getId();
      boolean success = ServerConnection.getServer().removeReview(userId, currentRestaurantId);
      
      if(success) System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Review removed successfully");
      else System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Failed to remove review");

      UserHomeController.getInstance().closeComment();
      UserHomeController.getInstance().loadReviews();
    } catch (RemoteException ex) {
      ex.printStackTrace();
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Server comunication");
    }
  }
  
  public void setComment(String[] c)
  {
    name_R.setText(c[0]);
    comment.setText(c[1]);
    answer.setText(c[2]);
    
    currentRestaurantId = Integer.parseInt(c[4]);

    int stars = Integer.parseInt(c[3]);
    switch(stars) {
      case 1:
        starsOne.setSelected(true);
        break;
      case 2:
        starsTwo.setSelected(true);
        break;
      case 3:
        starsThree.setSelected(true);
        break;
      default:
        starsOne.setSelected(true);
    }
  }
}
