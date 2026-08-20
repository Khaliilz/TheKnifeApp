package com.lab.controller.access;

import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.controller.user.UserHomeController;
import com.lab.database.model.Session;
import com.lab.database.model.User;
import com.lab.database.query.UserQ;
import com.lab.utility.Lib;

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
    ToolbarController.showLeftSide(false, false, false);
    UserHomeController.isGuest = false;
    
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

    if(username.isEmpty()) {
      Lib.errorBorder(username_TF);
      error = true;
    }

    if(password.isEmpty()) { 
      Lib.errorBorder(password_PF); 
      error = true; 
    }

    if(error) return;

    User user = UserQ.signin(username, password);

    if(user == null) {
      Lib.errorBorder(username_TF);
      Lib.errorBorder(password_PF);
    } else {
      System.out.println("[" + Lib.PURPLE + "DATABASE" + Lib.RESET + "] Singin completed [" + user.getUsername() + "]");
      Session.setCurrentUser(user);

      if(user.getRole().equals("CLIENTE")) PageController.selectPage("/com/lab/fxml/user/userHome.fxml");
      else if(user.getRole().equals("RISTORATORE")) PageController.selectPage("/com/lab/fxml/restaurateur/restaurateurHome.fxml");
    }
  }
}
