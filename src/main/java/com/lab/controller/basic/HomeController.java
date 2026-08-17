package com.lab.controller.basic;

import com.lab.Lib;
import com.lab.controller.user.UserHomeController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController {
  
  @FXML private Button signin_B;
  @FXML private Button signup_B;
  @FXML private Button guest_B;

  public void initialize()
  {
    PageController.showTitle(true);
    ToolbarController.showBackButton(false);
  }

  @FXML
  public void signinClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Signin button clicked");
    UserHomeController.isGuest = false;
    PageController.selectPage("/com/lab/fxml/access/signin.fxml");
  }

  @FXML
  public void signupClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Signup button clicked");
    PageController.selectPage("/com/lab/fxml/access/signup.fxml");
  }

  @FXML
  public void guestClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Guest button clicked");
    UserHomeController.isGuest = true;
    PageController.selectPage("/com/lab/fxml/access/guest.fxml");
  }
}
