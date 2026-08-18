package com.lab.controller.access;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.lab.Lib;
import com.lab.controller.basic.PageController;
import com.lab.controller.basic.ToolbarController;
import com.lab.controller.user.UserHomeController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class SignupController {
  
  @FXML private TextField nome_TF;
  @FXML private TextField cognome_TF;
  @FXML private TextField dataNascita_TF;
  @FXML private TextField domicilio_TF;
  @FXML private TextField username_TF;
  @FXML private PasswordField password_PF;
  @FXML private ToggleGroup roleGroup;
  @FXML private RadioButton customer;
  @FXML private RadioButton restaurateur;
  @FXML private Button signup_B;

  @FXML
  public void initialize()
  {
    PageController.showTitle(true);
    ToolbarController.setupBackButton(true, "/com/lab/fxml/basic/home.fxml");
    ToolbarController.showLeftSide(false, false, false);
    UserHomeController.isGuest = false;
    
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
    initialize();
    boolean error = false;

    String name = nome_TF.getText().trim();
    String surname = cognome_TF.getText().trim();
    String birthDate = dataNascita_TF.getText().trim();
    String address = domicilio_TF.getText().trim();
    String username = username_TF.getText();
    String password = password_PF.getText();
    String role = customer.isSelected()? "Cliente" : "Ristoratore";

    if(name.isEmpty()){ 
      Lib.errorBorder(nome_TF);
      error = true;
    }

    if(surname.isEmpty()){
      Lib.errorBorder(cognome_TF);
      error = true;
    }

    if(birthDate.isEmpty() || !birthDate.matches("\\d{2}/\\d{2}/\\d{4}") || !checkDataNascita(birthDate)){ 
      Lib.errorBorder(dataNascita_TF); 
      error = true; 
    }

    String addressRegex = "^[\\p{L}\\s\\'\\-\\.]+\\s*,\\s*[\\p{L}\\s\\'\\-\\.]+$";
    if(address.isEmpty() || !address.matches(addressRegex)){
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
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Signup completed [" + username + ", " + password + ", " + role + "]");
    if(role.equals("Cliente")) PageController.selectPage("/com/lab/fxml/user/userHome.fxml");
    else PageController.selectPage("/com/lab/fxml/restaurateur/restaurateurHome.fxml");
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
