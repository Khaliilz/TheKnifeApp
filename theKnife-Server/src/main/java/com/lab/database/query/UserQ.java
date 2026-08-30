/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
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

/**
 * Gestisce le interrogazioni al database relative alla funzionalità degli utenti.
 * <p>
 * Questa classe racchiude la logica di persistenza degli utenti. 
 * Sfrutta le API JDBC per comunicare con il database PostgreSQL, utilizzando i {@link PreparedStatement} 
 * per gestire in modo sicuro le operazioni sulle tabelle relazionali.
 * </p>
 */
public class UserQ {
  
  /**
   * Richiede l'utente che corrisponde alle credenziali inserite.
   * <p>
   * Esegue una query di tipo SELECT per ottenere un utente dalla tabella users.
   * Viene effettuato un controllo sulla correttezza dell'username e password.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param username Username.
   * @param password Password.
   * @return User se le credenziali sono corrette, null altrimenti (o in caso di errore di connessione).
   */
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

  /**
   * Richiede la registrazione di un nuovo utente.
   * <p>
   * Esegue una query di tipo INSERT per isnerire un utente dalla tabella users.
   * Nel caso venisse rilevato un errore di codice "23505", significa che il vincolo NOT UNIQUE e' stato infranto e che l'utente esiste gia'.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param name Nome dell'utente che effettua la richiesta.
   * @param surname Cognome dell'utente che effettua la richiesta.
   * @param birthDate Anno di nascita dell'utente che effettua la richiesta.
   * @param address Domicilio dell'utente che effettua la richiesta.
   * @param username Username dell'utente che effettua la richiesta.
   * @param plainPassword Password in chiaro dell'utente che effettua la richiesta.
   * @param role Ruolo dell'utente che effettua la richiesta.
   * @return true se la registrazione va a buon fine, false altrimenti (o in caso di errore di connessione).
   */
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
