package com.lab;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Lib {
  
  public static final String RESET = "\u001B[0m";
  public static final String RED = "\u001B[31m";
  public static final String GREEN = "\u001B[32m";
  public static final String YELLOW = "\u001B[33m";
  public static final String ORANGE = "\u001B[38;5;208m";
  public static final String BLUE = "\u001B[34m";
  public static final String PURPLE = "\u001B[35m";
  

  public static void drawGridLines(Pane root)
  {
    double width = root.getPrefWidth();
    double height = root.getPrefHeight();

    for(int i=0; i<width; i+=40){
      Line lineV = new Line(i, 0, i, height);
      lineV.setStroke(Color.RED);
      lineV.setStrokeWidth(1);
      lineV.setOpacity(0.3);
      root.getChildren().add(lineV);
    }

    for(int i=0; i<height; i+=50){
      Line lineH = new Line(0, i, width, i);
      lineH.setStroke(Color.RED);
      lineH.setStrokeWidth(1);
      lineH.setOpacity(0.3);
      root.getChildren().add(lineH);
    }

    Line lineC = new Line(0, height/2, width, height/2);
    lineC.setStroke(Color.BLUE);
    lineC.setStrokeWidth(3);
    lineC.setOpacity(0.3);
    root.getChildren().add(lineC);

    lineC = new Line(width/2, 0, width/2, height);
    lineC.setStroke(Color.BLUE);
    lineC.setStrokeWidth(3);
    lineC.setOpacity(0.5);
    root.getChildren().add(lineC);
  }

  public static void errorBorder(TextField field)
  {
    field.clear();
    if (!field.getStyleClass().contains("errorInput")) field.getStyleClass().add("errorInput");
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
