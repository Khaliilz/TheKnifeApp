package com.lab;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.lab.controller.SignupController;

public class SignupControllerTest {

    SignupController test = new SignupController();

    @Test
    public void dateTestYear()
    {
      String dateTest = "04/12/2027";
      
      assertFalse(test.checkDataNascita(dateTest), "Controllo fallito: anno maggiore dell'anno corrente"); 
    }

    @Test
    public void dateTestMonth1()
    {
      String dateTest = "04/13/2004";
      
      assertFalse(test.checkDataNascita(dateTest), "Controllo fallito: mese maggiore di 12"); 
    }

    @Test
    public void dateTestMonth2()
    {
      String dateTest = "04/0/2004";
      
      assertFalse(test.checkDataNascita(dateTest), "Controllo fallito: mese minore di 1"); 
    }
    
    @Test
    public void dateTestDay1()
    {
      String dateTest = "0/12/2004";
      
      assertFalse(test.checkDataNascita(dateTest), "Controllo fallito: giorno minore di 1"); 
    }

    @Test
    public void dateTestDay2()
    {
      String dateTest = "32/12/2004";
      
      assertFalse(test.checkDataNascita(dateTest), "Controllo fallito: giorno maggiore di 31");
    }

    @Test
    public void dateTestDay3()
    {
      String dateTest = "31/04/2004";
      
      assertFalse(test.checkDataNascita(dateTest), "Controllo fallito: giorno per mese"); 
    }

    @Test
    public void dateTestLeapYear1()
    {
      String dateTest = "29/2/2015";
      
      assertFalse(test.checkDataNascita(dateTest), "Controllo fallito: anno bisestile"); 
    }

    @Test
    public void dateTestLeapYear2()
    {
      String dateTest = "29/2/2008";
      
      assertTrue(test.checkDataNascita(dateTest), "Controllo fallito: anno bisestile divisibile per 4"); 
    }

    @Test
    public void dateTestLeapYear3()
    {
      String dateTest = "29/2/2000";
      
      assertTrue(test.checkDataNascita(dateTest), "Controllo fallito: anno bisestile divisibile per 400"); 
    }
}
