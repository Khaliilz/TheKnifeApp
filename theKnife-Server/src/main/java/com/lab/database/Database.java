/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configura le impostazioni del database per la connessione.
 */
public class Database {

  private static String dbUrl;
  private static String dbUser;
  private static String dbPassword;

  /**
   * Imposta i valori richiesti per trovare il database.
   * @param host Indirizzo IP e porta del database.
   * @param user Nome utente del database.
   * @param password Password del database.
   */
  public static void connect(String host, String user, String password)
  {
    dbUrl = "jdbc:postgresql://" + host + "/postgres"; 
    dbUser = user;
    dbPassword = password;
  }

  /**
   * Stabilisce la connessione con il database.
   * @return L'oggetto Connection che rappresenta la connessione attiva con il database.
   */
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
