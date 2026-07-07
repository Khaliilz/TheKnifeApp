package com.lab;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;

public class LibTest{
    
    @BeforeAll
    public static void initJFX() {
      try {
        Platform.startup(() -> {});
      } catch (IllegalStateException e) { }
    }
    
    @Test
    public void drawGridLinesTest(){
      Pane testPane = new Pane();
      testPane.setPrefWidth(200);
      testPane.setPrefHeight(200);     
      Lib.drawGridLines(testPane); 
      
      int expectedLines = 11;
      int actualLines = testPane.getChildren().size();     
      assertEquals(expectedLines, actualLines, "La griglia non ha generato il numero corretto di linee!");
    }

    @Test
    public void errorBorderTest()
		{
			TextField test = new TextField("Test");
			Lib.errorBorder(test);

			assertEquals("", test.getText(), "Il metodo non ha svuotato il testo");
			assertTrue(test.getStyleClass().contains("errorInput"), "La classe CSS (errorInput) non è stata aggiunta");
		}
}