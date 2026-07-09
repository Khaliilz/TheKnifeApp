package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SigninController {
  
  @FXML private Button signin_B;
  @FXML private TextField username_TF;
  @FXML private PasswordField password_PF;


  @FXML
  public void initialize()
  {
    
    PageController.showTitle(true);
    ToolbarController.setupBackButton(true, "home.fxml");
    password_PF.setOnAction(this::signinClicked);
    Lib.resetBorder(username_TF);
    Lib.resetBorder(password_PF);
  }

  @FXML
  public void signinClicked(ActionEvent event)
  {
    boolean error = false;
    String username = username_TF.getText();
    String password = password_PF.getText();

    if(username.isEmpty()){
      Lib.errorBorder(username_TF);
      error = true;
    }
    if(password.isEmpty() || password.length() < 8){ 
      Lib.errorBorder(password_PF); 
      error = true; 
    }

    if(error) return;
    System.out.println("[" + Lib.GREEN + "SCENE" + Lib.RESET + "] Signin completed [" + username + ", " + password + "]");
    PageController.selectPage("signinHome.fxml");
  }
}
