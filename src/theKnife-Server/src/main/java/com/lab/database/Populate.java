/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.database;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.lab.utility.StringColor;

/**
 * Populate
 * Si occupa di popolare il Database tramite i due metodi {@link #restaurants(String)} e {@link #users()}
 */
public class Populate {

  /**
   * Popola i ristoranti della relazione restaurants del database tramite query di tipo INSERT.
   * @param datasetPath Indirizzo del dataset contenente tutti i ristoranti.
   */
	public static void restaurants(String datasetPath)
	{
		String sql = "INSERT INTO restaurants (name, address, location, price, cuisine, latitude, longitude, delivery, booking) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try(InputStream is = Populate.class.getResourceAsStream(datasetPath)) {
      if(is == null) {
        System.out.println("[" + StringColor.RED + "DATABASE" + StringColor.RESET + "] Dataset not found");
        return;
      }

      try(InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
        
        connection.setAutoCommit(false);
        int count = 0;

        for(CSVRecord record : csvParser) {
          String name = record.get("Name");
          String address = record.get("Address");
          String location = record.get("Location");

          String price = record.get("Price");
					if(price != null) {
						price = price.replace("â‚¬", "€").replace("Â£", "£").replace("Â¥", "¥");
						price = price.replaceAll("[^€$£¥]", "€");
					}

          String cuisine = record.get("Cuisine");
            
          double longitude = 0.0;
          double latitude = 0.0;
          try{
            longitude = Double.parseDouble(record.get("Longitude"));
            latitude = Double.parseDouble(record.get("Latitude"));
          }catch(NumberFormatException ignored) { }

          String phone = record.isSet("PhoneNumber") ? record.get("PhoneNumber").trim() : "";
          String website = record.isSet("WebsiteUrl") ? record.get("WebsiteUrl").trim() : "";

          ps.setString(1, name);
          ps.setString(2, address);
          ps.setString(3, location);
          ps.setString(4, price);
          ps.setString(5, cuisine);
          ps.setDouble(6, latitude);
          ps.setDouble(7, longitude);
          ps.setString(8, phone);
          ps.setString(9, website);

          ps.addBatch();
          count++;

          if(count % 500 == 0)  ps.executeBatch();
        }

        ps.executeBatch();
        connection.commit();
        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Importati " + count + " ristoranti");
      }
    }catch (Exception e) {
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Importazione dei ristoranti fallita");
      e.printStackTrace();
    }
	}

  /**
   * Popola la tabella users con due utenti standard, un utente normale e un ristoratore tramite una query di tipo INSERT.
   */
  public static void users()
	{
		String sql = "INSERT INTO users (id, name, surname, birth_date, address, latitude, longitude, username, password, role) " +
                 " VALUES " +
                 " (1, 'user', 'user', '2004-12-04', 'legnano, milano', 45.5946464, 8.9181625, 'user', '$2a$10$vrQasz9Bz9ygIy7SvFz/beD3lyUIj1tZjTKTMhEhAaQgXlxi2TtFC', 'CLIENTE')," +
                 " (2, 'chef', 'chef', '2004-12-04', 'legnano, milano', 45.5946464, 8.9181625, 'chef', '$2a$10$kdMICAnsUNW58MsShhrksuQ.ncf9S.rykTt2/agKytGEYqQXnAim.', 'RISTORATORE');" +
                 "SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));";
		
    try(Connection connection = Database.getConnection(); Statement s = connection.createStatement()) {
      connection.setAutoCommit(false);

      s.execute(sql);
      
      connection.commit();
      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Creati i 2 utenti standard");
    }catch (Exception e) {
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Creazione dei 2 utenti standard fallita");
      e.printStackTrace();
    }
	}
}
