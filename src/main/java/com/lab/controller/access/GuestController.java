package com.lab.controller.access;

import com.lab.Lib;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class GuestController {
  
  @FXML private TextField luogo_TF;
  @FXML private Button cerca_B;

  @FXML
  public void initialize()
  {
    PageController.showTitle(true);
    ToolbarController.setupBackButton(true, "/com/lab/fxml/basic/home.fxml");
    luogo_TF.setOnAction(this::cercaClicked);
    Lib.resetBorder(luogo_TF);
  }

  @FXML
  public void cercaClicked(ActionEvent event)
  {
    String luogo = luogo_TF.getText().trim();
    if(luogo.isEmpty()){
      Lib.errorBorder(luogo_TF);
      return;
    }
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Guest serching [" + luogo + "]");
    PageController.selectPage("/com/lab/fxml/user/userHome.fxml");
  }
}
