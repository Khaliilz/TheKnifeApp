package com.lab.database;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.lab.utility.StringColor;

public class PopulateRestaurants {
  
	public static void importRestaurants(String datasetPath)
	{
		String sql = "INSERT INTO restaurants (name, address, location, price, cuisine, latitude, longitude, delivery, booking, award) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try(InputStream is = PopulateRestaurants.class.getResourceAsStream(datasetPath)) {
      if(is == null) {
        System.out.println("[" + StringColor.RED + "DATABASE" + StringColor.RESET + "] Dataset not found");
        return;
      }

      try(InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());
            Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

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
          String award = record.isSet("Award") ? record.get("Award").trim() : "";

          ps.setString(1, name);
          ps.setString(2, address);
          ps.setString(3, location);
          ps.setString(4, price);
          ps.setString(5, cuisine);
          ps.setDouble(6, latitude);
          ps.setDouble(7, longitude);
          ps.setString(8, phone);
          ps.setString(9, website);
          ps.setString(10, award);

          ps.addBatch();
          count++;

          if(count % 500 == 0)  ps.executeBatch();
        }

        ps.executeBatch();
        connection.commit();
        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Imported " + count + " restaurants");
      }
    }catch (Exception e) {
      System.out.println("[" + StringColor.RED + "DATABASE" + StringColor.RESET + "] Failed restaurants import");
      e.printStackTrace();
    }
	}
}
