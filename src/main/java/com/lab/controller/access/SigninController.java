package com.lab.controller.access;

import com.lab.Lib;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;

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
    ToolbarController.setupBackButton(true, "/com/lab/fxml/basic/home.fxml");
    ToolbarController.showSigninButton(false);
    ToolbarController.showSignupButton(false);
    ToolbarController.showSignoutButton(false);
    password_PF.setOnAction(this::signinClicked);
    Lib.resetBorder(username_TF);
    Lib.resetBorder(password_PF);
  }

  @FXML
  public void signinClicked(ActionEvent event)
  {
    initialize();
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
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Signin completed [" + username + ", " + password + "]");
    PageController.selectPage("/com/lab/fxml/user/userHome.fxml");
  }
}
