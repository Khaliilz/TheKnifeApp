package com.lab.controller;

import com.lab.Lib;

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
    PageController.selectPage("userHome.fxml");
  }

  @FXML
  public void signupClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Signup button clicked");
    PageController.selectPage("signup.fxml");
  }

  @FXML
  public void guestClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Guest button clicked");
    PageController.selectPage("guestHome.fxml");
  }
}
