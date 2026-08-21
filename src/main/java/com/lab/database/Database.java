package com.lab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.lab.utility.Lib;

public class Database {

  private static final String URL = "jdbc:postgresql://localhost:5432/theKnife";
  private static final String USER = "postgres";
  private static final String PASSWORD = "admin";

  public static Connection getConnection()
  {
    try {
      Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
      return connection;
    }catch(SQLException e) {
      System.out.println("[" + Lib.RED + "DATABASE" + Lib.RESET + "] Database connection failed");
      e.printStackTrace();
      return null;
    }
  }
}
