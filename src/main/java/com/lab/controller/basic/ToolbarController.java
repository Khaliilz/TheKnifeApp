package com.lab.controller.basic;

import com.lab.Lib;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ToolbarController {
  @FXML private StackPane toolbar;
  @FXML private Button exit_B;
  @FXML private Button minimize_B;
  @FXML private Button back_B;

  private static ToolbarController toolbarController;

  private double offsetX = 0;
  private double offsetY = 0;
  private String page = "/com/lab/fxml/basic/home.fxml";

  @FXML
  public void initialize() {

    toolbarController = this;

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

  public static void showBackButton(boolean show) {
    if(toolbarController != null) toolbarController.back_B.setVisible(show);
  }

  //
  public static void setupBackButton(boolean show, String prevPage) {
    if(toolbarController != null) {
      toolbarController.back_B.setVisible(show);
      toolbarController.page = prevPage;
    }
  }

  @FXML
  public void backClicked(ActionEvent event) {
    System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Backed to Home page");
    PageController.selectPage(page);
  }

  @FXML
  public void exitClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Application closed");
    Platform.exit();
  }

  @FXML
  public void minimizeClicked(ActionEvent event)
  {
    System.out.println("[" + Lib.BLUE + "ACTION" + Lib.RESET + "] Window minimized");
    Stage stage = (Stage) toolbar.getScene().getWindow();
    stage.setIconified(true);
  }
}
