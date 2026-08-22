package com.lab.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lab.database.Database;
import com.lab.utility.StringColor;

public class ReviewQ {
  
  public static List<String[]> getRestaurantReviews(int restaurantId)
  {
    List<String[]> list = new ArrayList<>();

    String sql = "SELECT users.name, reviews.stars, reviews.comment, reviews.answer " +
                  "FROM reviews " +
                  "JOIN users ON reviews.user_id = users.id " +
                  "WHERE reviews.restaurant_id = ?";

    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Cerco le recensioni del ristorante(" + restaurantId + ")...");
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

  public static String[] getUserReview(int userId, int restaurantId)
  {
    String sql = "SELECT stars, comment, answer FROM reviews WHERE user_id = ? AND restaurant_id = ?";
    
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Cerco le recensioni dell'utente(" + userId + ")...");
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

  public static boolean addReview(int userId, int restaurantId, int stars, String comment)
  {
    String sql = "INSERT INTO reviews (user_id, restaurant_id, stars, comment) VALUES (?, ?, ?, ?)";

    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Aggiungo la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")...");
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);
      ps.setInt(3, stars);
      ps.setString(4, comment);

      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Aggiunta la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")...");
      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Impossibile aggiungere la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")...");
      return false;
    }
  }

  public static boolean updateReview(int userId, int restaurantId, int stars, String comment)
  {
    String sql = "UPDATE reviews SET stars = ?, comment = ? WHERE user_id = ? AND restaurant_id = ?";
    
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Aggiorno la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")...");
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
      ps.setInt(1, stars);
      ps.setString(2, comment);
      ps.setInt(3, userId);
      ps.setInt(4, restaurantId);
      
      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Aggiornata la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")...");
      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Impossibile aggiornare la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")...");
      return false;
    }
  }

  public static boolean removeReview(int userId, int restaurantId)
  {
    String sql = "DELETE FROM reviews WHERE user_id = ? AND restaurant_id = ?";

    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Rimuovo la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")...");
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);

      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Rimossa la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")...");
      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Impossibile rimuovere la recensione dell'utente(" + userId + ") per il ristorante(" + restaurantId + ")...");
      return false;
    }
  }

  public static List<String[]> getRestaurateurReviews(int restaurantId)
  {
    List<String[]> list = new ArrayList<>();

    String sql = "SELECT users.name, reviews.stars, reviews.comment, reviews.answer, reviews.user_id " +
                 "FROM reviews " +
                 "JOIN users ON reviews.user_id = users.id " +
                 "WHERE reviews.restaurant_id = ?";

    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Cerco le recensioni del ristorante(" + restaurantId + ")...");
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

  public static boolean saveReviewAnswer(int userId, int restaurantId, String answer)
  {
    String sql = "UPDATE reviews SET answer = ? WHERE user_id = ? AND restaurant_id = ?";
    
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Salvo la risposta del ristoratore(" + restaurantId + ") alla recensione dell'utente(" + userId + ")...");
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
