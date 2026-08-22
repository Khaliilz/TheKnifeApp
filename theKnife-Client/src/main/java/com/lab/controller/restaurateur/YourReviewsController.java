package com.lab.controller.restaurateur;

import java.io.IOException;

import com.lab.App;
import com.lab.utility.StringColor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class YourReviewsController {
  
  @FXML private Text name;
  @FXML private Label comment;
  @FXML private Text starsNum;

  private String[] reviewData;

  public void setReviewData(String[] c)
  {
    reviewData = c;
    name.setText(c[0]);
    comment.setText(c[2]);
    starsNum.setText(c[1]);
  }

  @FXML public void answerClicked(ActionEvent event)
  {
    System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Answer button clicked");

    try{
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/lab/fxml/restaurateur/answerComment.fxml"));
      Parent root = loader.load();

      AnswerCommentController controller = loader.getController();
      controller.setReviewData(reviewData);

      Stage popupStage = new Stage();
      popupStage.setTitle("TheKnife - Answer");
      Image icon = new Image(getClass().getResource("/com/lab/img/logo.png").toExternalForm());
		  popupStage.getIcons().add(icon);
      popupStage.initStyle(StageStyle.TRANSPARENT); 
      popupStage.setResizable(false);
      popupStage.initModality(Modality.APPLICATION_MODAL);

      Scene scene = new Scene(root, 450, 530);
      scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
      popupStage.setScene(scene);
      popupStage.showAndWait();
    }catch (IOException ex) {
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Loading answer popup");
      ex.printStackTrace();
    }
  }
}
