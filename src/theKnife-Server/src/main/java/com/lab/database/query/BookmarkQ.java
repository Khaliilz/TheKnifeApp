/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.database.query;
import com.lab.database.Database;
import com.lab.utility.StringColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Gestisce le interrogazioni al database relative alla funzionalità dei ristoranti preferiti.
 * <p>
 * Questa classe racchiude la logica di persistenza per l'associazione tra utenti e ristoranti salvati. 
 * Sfrutta le API JDBC per comunicare con il database PostgreSQL, utilizzando i {@link PreparedStatement} 
 * per gestire in modo sicuro le operazioni sulle tabelle relazionali.
 * </p>
 */
public class BookmarkQ {
  
  /**
   * Verifica se un determinato ristorante e' già presente nella lista dei preferiti di un utente.
   * <p>
   * Esegue una query di tipo SELECT per controllare l'esistenza della relazione nella tabella bookmarks.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId L'id dell'utente che effettua la richiesta.
   * @param restaurantId L'id del ristorante da cercare.
   * @return true se il ristorante e' tra i preferiti dell'utente, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean isBookmarked(int userId, int restaurantId)
  {
    String sql = "SELECT 1 FROM bookmarks WHERE user_id = ? AND restaurant_id = ?";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);

      ResultSet rs = ps.executeQuery();
      return rs.next();
    } catch(Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Aggiunge un determinato ristorante nella lista dei preferiti di un utente.
   * <p>
   * Esegue una query di tipo INSERT per inserire la preferenza nella tabella bookmarks.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId L'id dell'utente che effettua la richiesta.
   * @param restaurantId L'id del ristorante preferito.
   * @return true se il ristorante viene salvato tra i preferiti dell'utente, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean addBookmark(int userId, int restaurantId)
  {
    String sql = "INSERT INTO bookmarks (user_id, restaurant_id) VALUES (?, ?)";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);

      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Ristorante(" + restaurantId + ") aggiunto ai preferiti dell'utente(" + userId + ")");
      return ps.executeUpdate() > 0;
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.PURPLE + "ERRORE" + StringColor.RESET + "] Impossibile aggiungere il ristorante(" + restaurantId + ") ai preferiti dell'utente(" + userId + ")");
      return false;
    }
  }

  /**
   * Rimuove un determinato ristorante dalla lista dei preferiti di un utente.
   * <p>
   * Esegue una query di tipo DELETE per eliminare un una preferenza di un utente su un ristorante nella tabella bookmarks.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId L'id dell'utente che effettua la richiesta.
   * @param restaurantId L'id del ristorante preferito.
   * @return true se il ristorante e' stato rimosso dai preferiti dell'utente, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean removeBookmark(int userId, int restaurantId)
  {
    String sql = "DELETE FROM bookmarks WHERE user_id = ? AND restaurant_id = ?";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);

      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Ristorante(" + restaurantId + ") rimosso dai preferiti dell'utente(" + userId + ")");
      return ps.executeUpdate() > 0;
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.PURPLE + "ERRORE" + StringColor.RESET + "] Impossibile rimuovere il ristorante(" + restaurantId + ") dai preferiti dell'utente(" + userId + ")");
      return false;
    }
  }
}
