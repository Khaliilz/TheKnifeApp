package com.lab.database.query;

import com.lab.database.Database;
import com.lab.database.model.Restaurant;
import com.lab.database.model.Session;
import com.lab.database.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RestaurantQ {
  
  private static String haversineFormula()
  {
    return "( 6371 * acos( cos( radians(?) ) * cos( radians( latitude ) ) * " +
           "cos( radians( longitude ) - radians(?) ) + sin( radians(?) ) * " +
           "sin( radians( latitude ) ) ) )";
  }

  public static List<Restaurant> getNearestRestaurants()
  {
    List<Restaurant> list = new ArrayList<>();
    double userLat = 0.0;
    double userLon = 0.0;

    User user = Session.getCurrentUser();
    if(user != null) {
      userLat = user.getLatitude();
      userLon = user.getLongitude();
    }

    String sql = "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, " + haversineFormula() + " AS distance, " + "COALESCE(AVG(reviews.stars), 0) AS avg_stars, " + "COUNT(reviews.id) AS total_reviews " +
                 "FROM restaurants " +
                 "LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
                 "GROUP BY restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, restaurants.latitude, restaurants.longitude " +
                 "ORDER BY distance ASC " +
                 "LIMIT 10";
  
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setDouble(1, userLat);
      ps.setDouble(2, userLon);
      ps.setDouble(3, userLat);
      
      ResultSet rs = ps.executeQuery();

      while(rs.next()) {
        Restaurant r = new Restaurant(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("address"),
          rs.getString("cuisine"),
          rs.getString("price"),
          rs.getDouble("distance"),
          rs.getString("delivery"),
          rs.getString("booking"),
          rs.getDouble("avg_stars"),
          rs.getInt("total_reviews")
        );
        list.add(r);
      }
    }catch(Exception e) { e.printStackTrace(); }

    return list;
  }

  public static List<Restaurant> getBookmarkedRestaurants()
  {
    List<Restaurant> list = new ArrayList<>();

    User user = Session.getCurrentUser();
    if(user == null) return list;

    double userLat = user.getLatitude();
    double userLon = user.getLongitude();
    int userId = user.getId();

    String sql = "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, " + haversineFormula() + " AS distance, " + "COALESCE(AVG(reviews.stars), 0) AS avg_stars, " + "COUNT(reviews.id) AS total_reviews " +
                 "FROM restaurants " +
                 "INNER JOIN bookmarks ON restaurants.id = bookmarks.restaurant_id LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
                 "WHERE bookmarks.user_id = ? " +
                 "GROUP BY restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, restaurants.latitude, restaurants.longitude " +
                 "ORDER BY distance ASC";
  
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setDouble(1, userLat);
      ps.setDouble(2, userLon);
      ps.setDouble(3, userLat);
      ps.setInt(4, userId);
      
      ResultSet rs = ps.executeQuery();

      while(rs.next()) {
        Restaurant r = new Restaurant(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("address"),
          rs.getString("cuisine"),
          rs.getString("price"),
          rs.getDouble("distance"),
          rs.getString("delivery"),
          rs.getString("booking"),
          rs.getDouble("avg_stars"),
          rs.getInt("total_reviews")
        );
        list.add(r);
      }
    }catch(Exception e) { e.printStackTrace(); }

    return list;
  }

  public static List<Restaurant> getReviewedRestaurants()
  {
    List<Restaurant> list = new ArrayList<>();

    User user = Session.getCurrentUser();
    if(user == null) return list;

    double userLat = user.getLatitude();
    double userLon = user.getLongitude();
    int userId = user.getId();

    String sql = "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, " + haversineFormula() + " AS distance, COALESCE(AVG(reviews.stars), 0) AS avg_stars, COUNT(reviews.id) AS total_reviews " +
                 "FROM restaurants " +
                 "INNER JOIN reviews my_rev ON restaurants.id = my_rev.restaurant_id LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +        // <-- Per la media totale
                 "WHERE my_rev.user_id = ? " +
                 "GROUP BY restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, restaurants.latitude, restaurants.longitude " +
                 "ORDER BY distance ASC";
  
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setDouble(1, userLat);
      ps.setDouble(2, userLon);
      ps.setDouble(3, userLat);
      ps.setInt(4, userId);
      
      ResultSet rs = ps.executeQuery();

      while(rs.next()) {
        Restaurant r = new Restaurant(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("address"),
          rs.getString("cuisine"),
          rs.getString("price"),
          rs.getDouble("distance"),
          rs.getString("delivery"),
          rs.getString("booking"),
          rs.getDouble("avg_stars"),
          rs.getInt("total_reviews")
        );
        list.add(r);
      }
    }catch(Exception e) { e.printStackTrace(); }

    return list;
  }

  public static List<Restaurant> getSerachedRestaurants(String place, String cuisine, String price, String delivery, String booking, String stars, int offset)
  {
    List<Restaurant> list = new ArrayList<>();
    double userLat = 0.0;
    double userLon = 0.0;

    User user = Session.getCurrentUser();
    if(user != null) {
      userLat = user.getLatitude();
      userLon = user.getLongitude();
    }


    StringBuilder sql = new StringBuilder(
      "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, " + haversineFormula() + " AS distance, COALESCE(AVG(reviews.stars), 0) AS avg_stars, COUNT(reviews.id) AS total_reviews " +
      "FROM restaurants " +
      "LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
      "WHERE 1=1 "
    );

    List<Object> params = new ArrayList<>();
    params.add(userLat);
    params.add(userLon);
    params.add(userLat);

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
      params.add(price);
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

    try(java.sql.Connection connection = Database.getConnection(); java.sql.PreparedStatement ps = connection.prepareStatement(sql.toString())) {
      
      for(int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
      
      java.sql.ResultSet rs = ps.executeQuery();
      
      while(rs.next()) {
        Restaurant r = new Restaurant(
          rs.getInt("id"),
          rs.getString("name"),
          rs.getString("address"),
          rs.getString("cuisine"),
          rs.getString("price"),
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

    return list;
  }
}
