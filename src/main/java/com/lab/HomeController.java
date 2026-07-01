package com.lab;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HomeController {
  
  @FXML private AnchorPane homepage;
  @FXML private StackPane toolbar;
  @FXML private Button signin_B;
  @FXML private Button signup_B;
  @FXML private Button guest_B;
  @FXML private Button exit_B;
  @FXML private Button minimize_B;

  private double offsetX = 0;
  private double offsetY = 0;

  @FXML
  public void initialize() {
      toolbar.setOnMousePressed(event -> {
          offsetX = event.getSceneX();
          offsetY = event.getSceneY();
      });

      toolbar.setOnMouseDragged(event -> {
          Stage stage = (Stage) toolbar.getScene().getWindow();
          stage.setX(event.getScreenX() - offsetX);
          stage.setY(event.getScreenY() - offsetY);
      });
  }

  @FXML
  public void signinClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "SIGNIN" + Lib.RESET + "]" + " signin scene..." + Lib.RESET);
  }

  @FXML
  public void signupClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "SIGNUP" + Lib.RESET + "]" + " signup scene..." + Lib.RESET);
  }

  @FXML
  public void guestClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.GREEN + "GUEST" + Lib.RESET + "]" + " guest scene..." + Lib.RESET);
  }

  @FXML
  public void exitClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.RED + "EXIT" + Lib.RESET + "]" + " homepage scene" + Lib.RESET);
    Platform.exit();
  }

  @FXML
  public void minimizeClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.ORANGE + "MINIMIZE" + Lib.RESET + "]" + " homepage scene" + Lib.RESET);
    Stage stage = (Stage) homepage.getScene().getWindow();
    stage.setIconified(true);
  }
}
