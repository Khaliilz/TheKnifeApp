/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lab.database.Database;
import com.lab.utility.StringColor;

/**
 * Gestisce le interrogazioni al database relative alla funzionalità delle recensioni.
 * <p>
 * Questa classe racchiude la logica di persistenza per l'associazione tra utenti, ristoranti e recensioni. 
 * Sfrutta le API JDBC per comunicare con il database PostgreSQL, utilizzando i {@link PreparedStatement} 
 * per gestire in modo sicuro le operazioni sulle tabelle relazionali.
 * </p>
 */
public class ReviewQ {
  
  /**
   * Richiede le recensioni di uno specifico ristorante.
   * <p>
   * Esegue una query di tipo SELECT per richiedere i ristoranti recensiti nella tabella reviews.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param restaurantId L'id del ristorante recensito.
   * @return Lista di ristoranti trovati, null altrimenti (o in caso di errore di connessione).
   */
  public static List<String[]> getRestaurantReviews(int restaurantId)
  {
    List<String[]> list = new ArrayList<>();

    String sql = "SELECT users.name, reviews.stars, reviews.comment, reviews.answer " +
                  "FROM reviews " +
                  "JOIN users ON reviews.user_id = users.id " +
                  "WHERE reviews.restaurant_id = ?";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setInt(1, restaurantId);

      ResultSet rs = ps.executeQuery();

      while(rs.next()) {
        String[] reviewData = new String[4];
        reviewData[0] = rs.getString("name");
        reviewData[1] = String.valueOf(rs.getInt("stars"));
        reviewData[2] = rs.getString("comment");
        reviewData[3] = rs.getString("answer");
        list.add(reviewData);
      }
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Richiesta lista recensioni del ristorante(" + restaurantId + ")");
    }

    System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Ottenuta la lista di recensioni del ristorante(" + restaurantId + ")");
    return list;
  }

  /**
   * Richiede le recensioni di uno specifico utente.
   * <p>
   * Esegue una query di tipo SELECT per richiedere i ristoranti recensiti da uno specifico utente dalla tabella reviews.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId Id dell'utente che effettua la richiesta.
   * @param restaurantId L'id del ristorante recensito.
   * @return Array di recensioni trovate, null altrimenti (o in caso di errore di connessione).
   */
  public static String[] getUserReview(int userId, int restaurantId)
  {
    String sql = "SELECT stars, comment, answer FROM reviews WHERE user_id = ? AND restaurant_id = ?";
    
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      
      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);
      
      ResultSet rs = ps.executeQuery();
            
      if(rs.next()){
        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Ottenuta la lista di recensioni dell'utente(" + userId + ")...");
        return new String[] { String.valueOf(rs.getInt("stars")), rs.getString("comment"), rs.getString("answer") };
      }
      }catch(Exception e) {
        e.printStackTrace();
        System.out.println("[" + StringColor.PURPLE + "ERRORE" + StringColor.RESET + "] Impossibile ottenere le recensioni dell'utente(" + userId + ")");
      }
      return null;
  }

  /**
   * Richiede l'aggiunta di una recensione di uno specifico ristorante.
   * <p>
   * Esegue una query di tipo INSERT per inserire una recensione nella tabella reviews.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId L'id dell'utente che ha scritto la recensione.
   * @param restaurantId L'id del ristorante recensito.
   * @param stars Stelle assegnate dall'utente.
   * @param comment Commento scritto dall'utente
   * @return true se l'operazione e' andata a buon fine, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean addReview(int userId, int restaurantId, int stars, String comment)
  {
    String sql = "INSERT INTO reviews (user_id, restaurant_id, stars, comment) VALUES (?, ?, ?, ?)";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);
      ps.setInt(3, stars);
      ps.setString(4, comment);

      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Aggiunta la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")");
      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Impossibile aggiungere la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")");
      return false;
    }
  }

  /**
   * Richiede l'aggiornamento di una recensione di uno specifico ristorante.
   * <p>
   * Esegue una query di tipo UPDATE per aggiornare una recensione nella tabella reviews.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId L'id dell'utente che ha scritto la recensione.
   * @param restaurantId L'id del ristorante recensito.
   * @param stars Stelle assegnate dall'utente.
   * @param comment Commento scritto dall'utente
   * @return true se l'operazione e' andata a buon fine, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean updateReview(int userId, int restaurantId, int stars, String comment)
  {
    String sql = "UPDATE reviews SET stars = ?, comment = ? WHERE user_id = ? AND restaurant_id = ?";
    
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
      ps.setInt(1, stars);
      ps.setString(2, comment);
      ps.setInt(3, userId);
      ps.setInt(4, restaurantId);
      
      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Aggiornata la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")");
      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Impossibile aggiornare la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")");
      return false;
    }
  }

  /**
   * Richiede la rimozione di una recensione di uno specifico ristorante.
   * <p>
   * Esegue una query di tipo DELETE per eliminare una recensione dalla tabella reviews.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId L'id dell'utente che ha scritto la recensione.
   * @param restaurantId L'id del ristorante recensito.
   * @return true se l'operazione e' andata a buon fine, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean removeReview(int userId, int restaurantId)
  {
    String sql = "DELETE FROM reviews WHERE user_id = ? AND restaurant_id = ?";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);

      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Rimossa la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")");
      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Impossibile rimuovere la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")");
      return false;
    }
  }

  /**
   * Richiede le recensione di uno specifico ristorante del ristoratore.
   * <p>
   * Esegue una query di tipo SELECT per richiedere le recensioni del proprio ristorante dalla tabella reviews.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param restaurantId L'id del ristorante per cui si effettua la richiesta.
   * @return Lista di recensioni ottenute, null altrimenti (o in caso di errore di connessione).
   */
  public static List<String[]> getRestaurateurReviews(int restaurantId)
  {
    List<String[]> list = new ArrayList<>();

    String sql = "SELECT users.name, reviews.stars, reviews.comment, reviews.answer, reviews.user_id " +
                 "FROM reviews " +
                 "JOIN users ON reviews.user_id = users.id " +
                 "WHERE reviews.restaurant_id = ?";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, restaurantId);
      
      ResultSet rs = ps.executeQuery();

      while(rs.next()) {
        String[] reviewData = new String[5];
        reviewData[0] = rs.getString("name");
        reviewData[1] = String.valueOf(rs.getInt("stars"));
        reviewData[2] = rs.getString("comment");
        reviewData[3] = rs.getString("answer");
        reviewData[4] = String.valueOf(rs.getInt("user_id"));

        list.add(reviewData);
      }
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Impossibile trovare le recensioni del ristorante(" + restaurantId + ")");
    }
    System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Ottenuta la lista di recensioni del ristorante(" + restaurantId + ")");
    return list;
  }

  /**
   * Richiede di salvataggio della risposta ad una recensione di uno specifico utente.
   * <p>
   * Esegue una query di tipo UPDATE per inserire una risposta ad una recensione nella tabella reviews.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId L'id dell'utente che ha scritto la recensione.
   * @param restaurantId L'id del ristorante recensito.
   * @param answer Risposta scritta dal ristoratore.
   * @return true se l'operazione e' andata a buon fine, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean saveReviewAnswer(int userId, int restaurantId, String answer)
  {
    String sql = "UPDATE reviews SET answer = ? WHERE user_id = ? AND restaurant_id = ?";
    
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, answer);
      ps.setInt(2, userId);
      ps.setInt(3, restaurantId);
      
      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Salvata la risposta alla recensione del ristorante(" + restaurantId + ")");
      return ps.executeUpdate() > 0;
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Impossibile salvare la risposta alla recensione del ristorante(" + restaurantId + ")");
      return false;
    }
  }

}
