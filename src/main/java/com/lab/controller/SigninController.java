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
    password_PF.setOnAction(this::signinClicked);
    ToolbarController.setupBackButton(true, "home.fxml");
  }

  @FXML
  public void signinClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "SIGNIN" + Lib.RESET + "]" + " Accesso completato");
  }
}
