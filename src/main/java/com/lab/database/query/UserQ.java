package com.lab.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.lab.database.Database;
import com.lab.database.model.User;
import com.lab.utility.Lib;
import com.lab.utility.PasswordHashing;

public class UserQ {
  
  public static User login(String username, String password)
  {
    String sql = "SELECT id, username, password, role FROM users WHERE username = ?";
        
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, username);
            
      ResultSet rs = ps.executeQuery();
            
      if(rs.next()) {
        String hashedPassword = rs.getString("password");
        
        if(PasswordHashing.checkPassword(password, hashedPassword)) return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
        else System.out.println("[" + Lib.RED + "DATABASE" + Lib.RESET + "] Wrong password");
      } else System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Wrong username");
      
    }catch(Exception e){
      System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Signin failed");
      e.printStackTrace();
    }
        
    return null;
  }
}
