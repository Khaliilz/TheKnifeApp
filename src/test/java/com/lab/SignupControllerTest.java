package com.lab;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import com.lab.controller.access.SignupController;

public class SignupControllerTest {

    private SignupController test = new SignupController();
    private LocalDate today = LocalDate.now();
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/uuuu");
 
    @Test
    public void dateTestFuture()
    {
      LocalDate dateTest = today.plusDays(1);
      String dateString = dateTest.format(format);
      
      assertFalse(test.checkDataNascita(dateString), "Fail: future date"); 
    }

    @Test
    public void dateTestMinorUser()
    {
      LocalDate dateTest = today.plusYears(-15);
      String dateString = dateTest.format(format);
      
      assertFalse(test.checkDataNascita(dateString), "Fail: minor user"); 
    }

    @Test
    public void dateTestOldUser()
    {
      LocalDate dateTest = today.plusYears(-91);
      String dateString = dateTest.format(format);
      
      assertFalse(test.checkDataNascita(dateString), "Fail: old user"); 
    }

    @Test
    public void dateTestCorrect()
    {
      LocalDate dateTest = today.plusYears(-20);
      String dateString = dateTest.format(format);
      
      assertTrue(test.checkDataNascita(dateString), "Fail: right date, but returned false"); 
    }
}
