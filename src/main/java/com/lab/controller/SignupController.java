package com.lab.controller;

import com.lab.Lib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SignupController {
  
  @FXML private TextField nome_TF;
  @FXML private TextField cognome_TF;
  @FXML private TextField dataNascita_TF;
  @FXML private TextField domicilio_TF;
  @FXML private TextField username_TF;
  @FXML private PasswordField password_PF;
  @FXML private ToggleGroup ruoloGroup;
  @FXML private RadioButton cliente_RB;
  @FXML private RadioButton ristoratore_RB;
  @FXML private Button signup_B;

  @FXML
  public void initialize()
  {
    PageController.showTitle(true);
    ToolbarController.setupBackButton(true, "home.fxml");
    password_PF.setOnAction(this::signupClicked);
    Lib.resetBorder(nome_TF);
    Lib.resetBorder(cognome_TF);
    Lib.resetBorder(dataNascita_TF);
    Lib.resetBorder(domicilio_TF);
    Lib.resetBorder(username_TF);
    Lib.resetBorder(password_PF);
  }

  @FXML
  public void signupClicked(ActionEvent event)
  {
    boolean error = false;

    String nome = nome_TF.getText().trim();
    String cognome = cognome_TF.getText().trim();
    String dataNascita = dataNascita_TF.getText().trim();
    String domicilio = domicilio_TF.getText().trim();
    String username = username_TF.getText();
    String password = password_PF.getText();
    String ruolo = cliente_RB.isSelected()? "Cliente" : "Ristoratore";

    if(nome.isEmpty()){ 
      Lib.errorBorder(nome_TF);
      error = true;
    }
    if(cognome.isEmpty()){
      Lib.errorBorder(cognome_TF);
      error = true;
    }
    if(dataNascita.isEmpty() || !dataNascita.matches("\\d{2}/\\d{2}/\\d{4}") || !checkDataNascita(dataNascita)){ 
      Lib.errorBorder(dataNascita_TF); 
      error = true; 
    }
    if(domicilio.isEmpty()){
      Lib.errorBorder(domicilio_TF);
      error = true;
    }
    if(username.isEmpty()){
      Lib.errorBorder(username_TF);
      error = true;
    }
    if(password.isEmpty() || password.length() < 8){ 
      Lib.errorBorder(password_PF); 
      error = true; 
    }

    if(error) return;
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Signup completed [" + username + ", " + password + ", " + ruolo + "]");
  }

  public boolean checkDataNascita(String date)
  {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    try{
      LocalDate dateParsed = LocalDate.parse(date, format);
      LocalDate today = LocalDate.now();
      long betweenDates = java.time.temporal.ChronoUnit.YEARS.between(dateParsed, today);
      if(dateParsed.isAfter(today)) return false;
      if(betweenDates >= 90 || betweenDates <= 16) return false;
    }catch(java.time.format.DateTimeParseException e) { return false; }

    return true;
  }
}
