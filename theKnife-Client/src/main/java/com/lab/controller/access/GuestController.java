package com.lab.controller.access;

import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.controller.user.UserHomeController;
import com.lab.utility.StringColor;
import com.lab.utility.ErrorContainer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class GuestController {
  
  @FXML private TextField place_TF;
  @FXML private Button searchButton;

  @FXML
  public void initialize()
  {
    PageController.showTitle(true);
    ToolbarController.setupBackButton(true, "/com/lab/fxml/basic/home.fxml");
    ToolbarController.showLeftSide(false, false, false);
    place_TF.setOnAction(this::searchClicked);
    ErrorContainer.resetBorder(place_TF);
  }

  @FXML
  public void searchClicked(ActionEvent event)
  {
    boolean error = false;

    String place = place_TF.getText().trim();
    if(place.isEmpty()) {
      ErrorContainer.errorBorder(place_TF);
      error = true;
    }

    if(error) return;
    UserHomeController.guestSearchPlace = place;
    PageController.selectPage("/com/lab/fxml/user/userHome.fxml");
  }
}
