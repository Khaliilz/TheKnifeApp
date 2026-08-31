/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.database.query;

import com.lab.utility.StringColor;
import com.lab.utility.PriceConverter;

import com.lab.database.Database;
import com.lab.model.Restaurant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce le interrogazioni al database relative alla funzionalità dei ristoranti.
 * <p>
 * Questa classe racchiude la logica di persistenza per l'associazione tra ristoranti, recensioni e utenti. 
 * Sfrutta le API JDBC per comunicare con il database PostgreSQL, utilizzando i {@link PreparedStatement} 
 * per gestire in modo sicuro le operazioni sulle tabelle relazionali.
 * </p>
 */
public class RestaurantQ {
  
  private static String haversineFormula()
  {
    return "( 6371 * acos( cos( radians(?) ) * cos( radians( latitude ) ) * " +
           "cos( radians( longitude ) - radians(?) ) + sin( radians(?) ) * " +
           "sin( radians( latitude ) ) ) )";
  }

  /**
   * Verifica se la relazione restaurants e' vuota per l'eventuale popolamento.
   * <p>
   * Esegue una query di tipo SELECT per controllare l'esistenza dei ristoranti.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @return true se la relazione e' vuota, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean isDatabaseEmpty()
  {
    String sql = "SELECT COUNT(*) AS total FROM restaurants";
    
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ResultSet rs = ps.executeQuery();
         
      if(rs.next()) {
        int count = rs.getInt("total");
        return count == 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Impossibile verificare lo stato del DB");
    }
    return true;
  }

  /**
   * Richiede i ristoranti piu' vicini all'utente tramite coordinate.
   * <p>
   * Esegue una query di tipo SELECT per selezionare i ristoranti.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param lat Latitudine dell'utente.
   * @param lon Longitudine dell'utente.
   * @return lista di ristoranti trovati, null altrimenti (o in caso di errore di connessione).
   */
  public static List<Restaurant> getNearestRestaurants(double lat, double lon)
  {
    List<Restaurant> list = new ArrayList<>();

    String sql = "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, " + haversineFormula() + " AS distance, " + "COALESCE(AVG(reviews.stars), 0) AS avg_stars, " + "COUNT(reviews.id) AS total_reviews " +
                 "FROM restaurants " +
                 "LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
                 "GROUP BY restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, restaurants.latitude, restaurants.longitude " +
                 "ORDER BY distance ASC " +
                 "LIMIT 10";
    
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setDouble(1, lat);
      ps.setDouble(2, lon);
      ps.setDouble(3, lat);
      
      ResultSet rs = ps.executeQuery();

      while(rs.next()) {
        Restaurant r = new Restaurant(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("address"),
          rs.getString("cuisine"),
          PriceConverter.symbolsToPrice(rs.getString("price")),
          rs.getDouble("distance"),
          rs.getString("delivery"),
          rs.getString("booking"),
          rs.getDouble("avg_stars"),
          rs.getInt("total_reviews")
        );
        list.add(r);
      }
    }catch(Exception e) { e.printStackTrace(); }

    System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] lista di ristoranti vicini ottenuta");
    return list;
  }

  /**
   * Richiede i ristoranti preferiti dall'utente e li ordina in base alla loro distanza dall'utente tramite coordinate.
   * <p>
   * Esegue una query di tipo SELECT per selezionare i ristoranti.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId L'id dell'utente che effettua la richiesta.
   * @param lat Latitudine dell'utente.
   * @param lon Longitudine dell'utente.
   * @return lista di ristoranti trovati, null altrimenti (o in caso di errore di connessione).
   */
  public static List<Restaurant> getBookmarkedRestaurants(int userId, double lat, double lon)
  {
    List<Restaurant> list = new ArrayList<>();

    String sql = "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, " + haversineFormula() + " AS distance, " + "COALESCE(AVG(reviews.stars), 0) AS avg_stars, " + "COUNT(reviews.id) AS total_reviews " +
                 "FROM restaurants " +
                 "INNER JOIN bookmarks ON restaurants.id = bookmarks.restaurant_id LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
                 "WHERE bookmarks.user_id = ? " +
                 "GROUP BY restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, restaurants.latitude, restaurants.longitude " +
                 "ORDER BY distance ASC";
    
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setDouble(1, lat);
      ps.setDouble(2, lon);
      ps.setDouble(3, lat);
      ps.setInt(4, userId);
      
      ResultSet rs = ps.executeQuery();

      while(rs.next()) {
        Restaurant r = new Restaurant(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("address"),
          rs.getString("cuisine"),
          PriceConverter.symbolsToPrice(rs.getString("price")),
          rs.getDouble("distance"),
          rs.getString("delivery"),
          rs.getString("booking"),
          rs.getDouble("avg_stars"),
          rs.getInt("total_reviews")
        );
        list.add(r);
      }
    }catch(Exception e) { e.printStackTrace(); }
    
    System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] lista di ristoranti preferiti ottenuta");
    return list;
  }

  /**
   * Richiede i ristoranti recensiti dall'utente e li ordina in base alla loro distanza dall'utente tramite coordinate.
   * <p>
   * Esegue una query di tipo SELECT per selezionare i ristoranti.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param userId L'id dell'utente che effettua la richiesta.
   * @param lat Latitudine dell'utente.
   * @param lon Longitudine dell'utente.
   * @return lista di ristoranti trovati, null altrimenti (o in caso di errore di connessione).
   */
  public static List<Restaurant> getReviewedRestaurants(int userId, double lat, double lon)
  {
    List<Restaurant> list = new ArrayList<>();

    String sql = "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, " + haversineFormula() + " AS distance, COALESCE(AVG(reviews.stars), 0) AS avg_stars, COUNT(reviews.id) AS total_reviews " +
                 "FROM restaurants " +
                 "INNER JOIN reviews my_rev ON restaurants.id = my_rev.restaurant_id LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
                 "WHERE my_rev.user_id = ? " +
                 "GROUP BY restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, restaurants.latitude, restaurants.longitude " +
                 "ORDER BY distance ASC";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setDouble(1, lat);
      ps.setDouble(2, lon);
      ps.setDouble(3, lat);
      ps.setInt(4, userId);
      
      ResultSet rs = ps.executeQuery();

      while(rs.next()) {
        Restaurant r = new Restaurant(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("address"),
          rs.getString("cuisine"),
          PriceConverter.symbolsToPrice(rs.getString("price")),
          rs.getDouble("distance"),
          rs.getString("delivery"),
          rs.getString("booking"),
          rs.getDouble("avg_stars"),
          rs.getInt("total_reviews")
        );
        list.add(r);
      }
    }catch(Exception e) { e.printStackTrace(); }

    System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] lista di recensioni ottenuta");
    return list;
  }

  /**
   * Richiede i ristoranti cercati dall'utente in base ad un eventuale filtro e li ordina in base alla loro distanza dall'utente tramite coordinate.
   * <p>
   * Esegue una query di tipo SELECT per selezionare i ristoranti.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param place Luogo in cui cercare.
   * @param cuisine Tipologia di cucina.
   * @param price Fascia di prezzo media.
   * @param delivery Disponibilita' della consegna a domicilio.
   * @param booking Disponibilita' di prenotazione.
   * @param stars Fascia di stelle medie.
   * @param offset Valore di offset dei valori trovati.
   * @param lat Latitudine dell'utente.
   * @param lon Longitudine dell'utente.
   * @return lista di ristoranti trovati, null altrimenti (o in caso di errore di connessione).
   */
  public static List<Restaurant> getSerachedRestaurants(String place, String cuisine, String price, String delivery, String booking, String stars, int offset, double lat, double lon)
  {
    List<Restaurant> list = new ArrayList<>();

    StringBuilder sql = new StringBuilder(
      "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, " + haversineFormula() + " AS distance, COALESCE(AVG(reviews.stars), 0) AS avg_stars, COUNT(reviews.id) AS total_reviews " +
      "FROM restaurants " +
      "LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
      "WHERE 1=1 "
    );

    List<Object> params = new ArrayList<>();
    params.add(lat);
    params.add(lon);
    params.add(lat);

    if(place != null && !place.trim().isEmpty()) {
      sql.append("AND (restaurants.address ILIKE ? OR restaurants.address ILIKE ? OR restaurants.address ILIKE ? OR restaurants.address ILIKE ? OR restaurants.address ILIKE ?) ");
      String p = place.trim();
      params.add(p);               
      params.add("%, " + p);
      params.add("%," + p); 
      params.add("%, " + p + ",%");
      params.add("%," + p + ",%");
    }

    if(cuisine != null) {
      sql.append("AND restaurants.cuisine ILIKE ? ");
      params.add("%" + cuisine.trim() + "%");
    }

    if(price != null) {
      sql.append("AND restaurants.price = ? ");
      params.add(PriceConverter.priceToSymbols(price));
    }

    if(delivery != null) {
      if (delivery.equalsIgnoreCase("Si")) {
          sql.append("AND restaurants.delivery IS NOT NULL AND restaurants.delivery != '' ");
      } else {
          sql.append("AND (restaurants.delivery IS NULL OR restaurants.delivery = '') ");
      }
    }

    if(booking != null) {
      if (booking.equalsIgnoreCase("Si")) {
          sql.append("AND restaurants.booking IS NOT NULL AND restaurants.booking != '' ");
      } else {
          sql.append("AND (restaurants.booking IS NULL OR restaurants.booking = '') ");
      }
    }

    sql.append("GROUP BY restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, restaurants.latitude, restaurants.longitude ");

    if(stars != null) {
      sql.append("HAVING COALESCE(AVG(reviews.stars), 0) >= ? ");
      double starsLimit = Double.parseDouble(stars);
      if(starsLimit == 1.0) {
          starsLimit = 0.0;
      }
      params.add(starsLimit);
    }

    sql.append("ORDER BY distance ASC LIMIT 10 OFFSET ?");
    params.add(offset);
    
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql.toString())) {
      
      for(int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
      
      ResultSet rs = ps.executeQuery();
      
      while(rs.next()) {
        Restaurant r = new Restaurant(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("address"),
          rs.getString("cuisine"),
          PriceConverter.symbolsToPrice(rs.getString("price")),
          rs.getDouble("distance"),
          rs.getString("delivery"),
          rs.getString("booking"),
          rs.getDouble("avg_stars"),
          rs.getInt("total_reviews")
        );

        list.add(r);
      }
    }catch(Exception e) { 
      e.printStackTrace();
    }
    
    System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] lista di ristoranti a " + place + " ottenuta");
    return list;
  }

  /**
   * Richiede i ristoranti del ristoratore.
   * <p>
   * Esegue una query di tipo SELECT per selezionare i ristoranti.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param ownerId L'id del ristoratore che effettua la richiesta.
   * @return lista di ristoranti trovati, null altrimenti (o in caso di errore di connessione).
   */
  public static List<Restaurant> getRestaurantsByOwner(int ownerId)
  {
    List<Restaurant> list = new ArrayList<>();
    String sql = "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, 0.0 AS distance, COALESCE(AVG(reviews.stars), 0) AS avg_stars, COUNT(reviews.id) AS total_reviews " +
                 "FROM restaurants " +
                 "LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
                 "WHERE restaurants.owner_id = ? " +
                 "GROUP BY restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, restaurants.latitude, restaurants.longitude " +
                 "ORDER BY restaurants.id DESC";
  
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, ownerId);
      
      ResultSet rs = ps.executeQuery();
      
      while(rs.next()) {
        list.add(new Restaurant(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("address"),
          rs.getString("cuisine"),
          PriceConverter.symbolsToPrice(rs.getString("price")),
          rs.getDouble("distance"),
          rs.getString("delivery"),
          rs.getString("booking"),
          rs.getDouble("avg_stars"),
          rs.getInt("total_reviews")
        ));
      }
    }catch(Exception e) {
      e.printStackTrace();
    }

    System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] lista di ristoranti del ristoratore(" + ownerId + ") ottenuta");
    return list;
  }

  /**
   * Richiesta di aggiunta di un nuovo ristorante.
   * <p>
   * Esegue una query di tipo INSERT per inserire il ristorante.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param name Nome del ristorante.
   * @param address Indirizzo del ristorante.
   * @param cuisine Tipologia di cucina.
   * @param price Fascia di prezzo media.
   * @param delivery Disponibilita' della consegna a domicilio.
   * @param booking Disponibilita' di prenotazione.
   * @param lat Latitudine del ristorante.
   * @param lon Longitudine del rsitorante.
   * @param ownerId id del proprietario del ristorante.
   * @return true se l'operazione va a buon fine, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean addRestaurant(String name, String address, String cuisine, String price, String delivery, String booking, double lat, double lon, int ownerId)
  {
    String sql = "INSERT INTO restaurants (name, address, cuisine, price, delivery, booking, latitude, longitude, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Aggiungo un nuovo ristorante del ristoratore(" + ownerId + ") ...");
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, name);
      ps.setString(2, address);
      ps.setString(3, cuisine);
      ps.setString(4, PriceConverter.priceToSymbols(price));
      ps.setString(5, delivery);
      ps.setString(6, booking);
      ps.setDouble(7, lat);
      ps.setDouble(8, lon);
      ps.setInt(9, ownerId);
      
      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Aggiunto il ristorante " + name + " del ristoratore(" + ownerId + ")");
      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Aggiunta nuovo ristorante fallita");
      return false;
    }
  }

  /**
   * Rimuove un ristorante del ristoratore.
   * <p>
   * Esegue una query di tipo DELETE per eliminare un ristorante specifico.
   * La connessione viene recuperata dinamicamente e chiusa automaticamente grazie al blocco try-with-resources.
   * </p>
   * 
   * @param restaurantId L'id del ristorante da eliminare.
   * @return true se l'operazione va a buon fine, false altrimenti (o in caso di errore di connessione).
   */
  public static boolean removeRestaurant(int restaurantId)
  {
    String sql = "DELETE FROM restaurants WHERE id = ?";
    
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, restaurantId);
      
      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Rimosso il ristorante(" + restaurantId + ")");
      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Rimozione ristorante fallita");
      return false;
    }
  }
}
