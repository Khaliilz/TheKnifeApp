package com.lab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.lab.utility.StringColor;

public class Database {

  private static String dbUrl;
  private static String dbUser;
  private static String dbPassword;

  public static void connect(String host, String user, String password)
  {
    dbUrl = "jdbc:postgresql://" + host + "/theKnife"; 
    dbUser = user;
    dbPassword = password;
  }

  public static Connection getConnection()
  {
    try {
      Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
      return connection;
    } catch(SQLException e) {
      return null;
    }
  }
}
