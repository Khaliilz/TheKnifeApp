package com.lab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

  public static Connection getConnection() throws SQLException
  {
    if(dbUrl == null) throw new SQLException("Database not initialized. Wrong server credentials");
    return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
  }
}
