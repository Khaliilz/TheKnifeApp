package com.lab.database;

import java.sql.Connection;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import com.lab.utility.StringColor;

public class DatabaseInit {

  public static void initialize()
  {

    String fileName = "/com/lab/data/theKnifeSchema.sql";

    try(InputStream is = DatabaseInit.class.getResourceAsStream(fileName)) {
      if(is == null) {
        System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Schema SQL non trovato");
        return;
      }

      String sqlContent = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));

      String[] queries = sqlContent.split(";");

      try(Connection connection = Database.getConnection(); Statement s = connection.createStatement()) {
        for(String q : queries) 
          if(!q.trim().isEmpty()) s.execute(q.trim());

        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Schema caricato nel DB");
      }
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Errore durante l'inizializzazione dello schema");
    }
  }
}