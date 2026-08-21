package com.lab.utility;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ErrorContainer {

  public static void errorBorder(TextField field)
  {
    field.clear();
    if (!field.getStyleClass().contains("errorInput")) field.getStyleClass().add("errorInput");
  }

  public static void errorBorder(DatePicker field)
  {
    field.setValue(null);
    if (!field.getStyleClass().contains("errorInput")) field.getStyleClass().add("errorInput");
  }

  public static void resetBorder(DatePicker field)
  {
    field.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
      if(isNowFocused) field.getStyleClass().remove("errorInput");
    });

    field.valueProperty().addListener((observable, oldValue, newValue) -> {
      field.getStyleClass().remove("errorInput");
    });

    field.setOnMouseClicked(event -> {
      field.getStyleClass().remove("errorInput");
    });
  }

  public static void resetBorder(TextField field)
  {
    field.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
      if(isNowFocused) field.getStyleClass().remove("errorInput");
    });

    field.textProperty().addListener((observable, oldValue, newValue) -> {
      field.getStyleClass().remove("errorInput");
    });

    field.setOnMouseClicked(event -> {
      field.getStyleClass().remove("errorInput");
    });
  }

}
