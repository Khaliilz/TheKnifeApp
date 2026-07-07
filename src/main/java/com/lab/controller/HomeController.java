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
    ToolbarController.showBackButton(false);
  }

  @FXML
  public void signinClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "SIGNIN" + Lib.RESET + "]" + " Signin scene");
    PageController.selectPage("signin.fxml");
  }

  @FXML
  public void signupClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "SIGNUP" + Lib.RESET + "]" + " Signup scene");
    PageController.selectPage("signup.fxml");
  }

  @FXML
  public void guestClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "GUEST" + Lib.RESET + "]" + " Guest scene");
  }
}
