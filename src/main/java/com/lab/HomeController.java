package com.lab;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController {
  
  @FXML private AnchorPane homepage;
  @FXML private Button signin_B;
  @FXML private Button signup_B;
  @FXML private Button guest_B;
  @FXML private Button exit_B;
  @FXML private Button minimize_B;

  @FXML
  public void signinClicked(ActionEvent event)
  {
    System.out.println("Sign in scene...");
    signin_B.setText("OK");
  }

  @FXML
  public void signupClicked(ActionEvent event)
  {
    System.out.println("Sign up scene...");
    signup_B.setText("OK");
  }

  @FXML
  public void guestClicked(ActionEvent event)
  {
    System.out.println("Guest scene...");
    guest_B.setText("OK");
  }

  @FXML
  public void exitClicked(ActionEvent event)
  {
    System.out.println("Exit from scene...");
    Platform.exit();
  }

  @FXML
  public void minimizeClicked(ActionEvent event)
  {
    System.out.println("Minimize scene...");
    Stage stage = (Stage) homepage.getScene().getWindow();
    stage.setIconified(true);
  }
}
