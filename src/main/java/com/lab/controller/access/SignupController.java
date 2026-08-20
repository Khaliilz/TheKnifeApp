package com.lab.controller.access;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class SignupController {
  
  @FXML private TextField name_TF;
  @FXML private TextField surname_TF;
  @FXML private DatePicker birthDate;
  @FXML private TextField address_TF;
  @FXML private TextField username_TF;
  @FXML private PasswordField password_PF;
  @FXML private RadioButton customer;
  @FXML private RadioButton restaurateur;
  @FXML private Button signup_B;

  @FXML
  public void initialize()
  {
    PageController.showTitle(true);
    ToolbarController.setupBackButton(true, "/com/lab/fxml/basic/home.fxml");
    ToolbarController.showLeftSide(false, false, false);
    
    password_PF.setOnAction(this::signupClicked);
    Lib.resetBorder(name_TF);
    Lib.resetBorder(surname_TF);
    Lib.resetBorder(birthDate);
    Lib.resetBorder(address_TF);
    Lib.resetBorder(username_TF);
    Lib.resetBorder(password_PF);
  }

  @FXML
  public void signupClicked(ActionEvent event)
  {
    boolean error = false;

    String name = name_TF.getText().trim();
    String surname = surname_TF.getText().trim();

    LocalDate date = birthDate.getValue();
    if(date == null && !birthDate.getEditor().getText().trim().isEmpty()) {
      try{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date = LocalDate.parse(birthDate.getEditor().getText().trim(), formatter);
        birthDate.setValue(date);
      }catch(Exception e) {
        date = null;
      }
    }
    
    String address = address_TF.getText().trim().toLowerCase();
    String username = username_TF.getText();
    String password = password_PF.getText();
    String role = customer.isSelected()? "CLIENTE" : "RISTORATORE";

    if(name.isEmpty()) { 
      Lib.errorBorder(name_TF);
      error = true;
    }

    if(surname.isEmpty()) {
      Lib.errorBorder(surname_TF);
      error = true;
    }

    java.sql.Date sqlDate = null;
    if(date == null || !checkBirthDate(date)) {
      Lib.errorBorder(birthDate);
      error = true;
    } else {
      sqlDate = java.sql.Date.valueOf(date);
    }

    String addressRegex = "^[\\p{L}\\s\\'\\-\\.]+\\s*,\\s*[\\p{L}\\s\\'\\-\\.]+$";
    if(address.isEmpty() || !address.matches(addressRegex)) {
      Lib.errorBorder(address_TF);
      error = true;
    }

    if(username.isEmpty()) {
      Lib.errorBorder(username_TF);
      error = true;
    }

    if(password.isEmpty() || password.length() < 8) { 
      Lib.errorBorder(password_PF); 
      error = true; 
    }

    if(error) return;

    boolean signup = UserQ.signup(name, surname, sqlDate, address, username, password, role);
    if(signup) {
      System.out.println("[" + Lib.PURPLE + "DATABASE" + Lib.RESET + "] Signup completed");
      User loggedUser = UserQ.signin(username, password);
      if(loggedUser != null){
        Session.setCurrentUser(loggedUser);
        if(loggedUser.getRole().equals("CLIENTE")) PageController.selectPage("/com/lab/fxml/user/userHome.fxml");
        else if(loggedUser.getRole().equals("RISTORATORE")) PageController.selectPage("/com/lab/fxml/restaurateur/restaurateurHome.fxml");
      }
      
    } else {
      Lib.errorBorder(username_TF);
    }
  }

  public boolean checkBirthDate(LocalDate d)
  {
    LocalDate today = LocalDate.now();
    long age = java.time.temporal.ChronoUnit.YEARS.between(d, today);

    if(d.isAfter(today)) return false;
    if(age > 90 || age < 16) return false;

    return true;
  }
}
