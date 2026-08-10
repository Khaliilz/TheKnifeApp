package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class WriteCommentController {
  @FXML private Text name_R;
  @FXML private TextArea comment;
  @FXML private ToggleGroup starsGroup;

  public void setRestaurantName(String name)
  {
    name_R.setText(name);
  }

  @FXML
  public void saveClicked(ActionEvent e)
  {
    RadioButton selectedRadioButton = (RadioButton) starsGroup.getSelectedToggle();

    System.out.println("[" + Lib.BLUE + "ACTION] " + Lib.RESET + "Save pressed [" + comment.getText() + ", " + selectedRadioButton.getText() + "]");

    SignedinDefaultController.getInstance().closeWriteComment();
  }
}
