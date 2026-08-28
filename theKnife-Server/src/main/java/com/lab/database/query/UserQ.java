package com.lab.database.query;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.lab.database.Database;
import com.lab.model.User;
import com.lab.utility.Geocoding;
import com.lab.utility.StringColor;
import com.lab.utility.PasswordHashing;

public class UserQ {
  
  public static User signin(String username, String password)
  {
    String sql = "SELECT id, username, password, role, address, latitude, longitude FROM users WHERE username = ?";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, username);
            
      ResultSet rs = ps.executeQuery();
            
      if(rs.next()) {
        String hashedPassword = rs.getString("password");
        
        if(PasswordHashing.checkPassword(password, hashedPassword)) {
          System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Utente " + username + " trovato e password verificata");
          return new User(rs.getInt("id"),
                          rs.getString("username"),
                          rs.getString("address"),
                          rs.getDouble("latitude"),
                          rs.getDouble("longitude"),
                          rs.getString("role")
                        );
        } else System.out.println("[" + StringColor.RED + "DATABASE" + StringColor.RESET + "] Password errata");
      } else System.out.println("[" + StringColor.RED + "DATABASE" + StringColor.RESET + "] Username errato");
      
    }catch(Exception e) {
      System.out.println("[" + StringColor.RED + "DATABASE" + StringColor.RESET + "] Registrazione fallita");
      e.printStackTrace();
    }
        
    return null;
  }

  public static boolean signup(String name, String surname, Date birthDate, String address, String username, String plainPassword, String role)
  {
    double lat = 0.0;
    double lon = 0.0;
    double[] coords = Geocoding.getCoordinates(address);
    if(coords != null) {
      lat = coords[0];
      lon = coords[1];
    } else System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Impossibile l'indirizzo in coordinate");

    String sql = "INSERT INTO users (name, surname, birth_date, address, latitude, longitude, username, password, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, name);
      ps.setString(2, surname);
      ps.setDate(3, birthDate);
      ps.setString(4, address);
      ps.setDouble(5, lat);
      ps.setDouble(6, lon);
      ps.setString(7, username);
      ps.setString(8, PasswordHashing.hashPassword(plainPassword));
      ps.setString(9, role);
            
      int insertRow = ps.executeUpdate();
      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Utente " + username + " registrato");
      return insertRow > 0;
            
      }catch(java.sql.SQLException e) {
        if("23505".equals(e.getSQLState())) {
          System.out.println("[" + StringColor.RED + "DATABASE" + StringColor.RESET + "] Username gia' esistente");
        } else {
          System.out.println("[" + StringColor.RED + "DATABASE" + StringColor.RESET + "] Registrazione nel DB fallita");
          e.printStackTrace();
        }
        return false;
      }
  }
}
