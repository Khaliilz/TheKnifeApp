package com.lab;

import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LibTest {

    @Test
    public void testDrawGridLines() {
        Pane testPane = new Pane();
        testPane.setPrefWidth(200);
        testPane.setPrefHeight(200);

        Lib.drawGridLines(testPane);
        
        int expectedLines = 11;
        int actualLines = testPane.getChildren().size();

        assertEquals(expectedLines, actualLines, "La griglia non ha generato il numero corretto di linee!");
    }
}