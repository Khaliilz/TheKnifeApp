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

    User user = Session.getCurrentUser();
    if(user == null) return list;

    double userLat = user.getLatitude();
    double userLon = user.getLongitude();

    String sql = "SELECT restaurants.id, restaurants.name, restaurants.address, restaurants.cuisine, restaurants.price, restaurants.delivery, restaurants.booking, " + haversineFormula() + " AS distance, " + "COALESCE(AVG(reviews.stars), 0) AS avg_stars, " + "COUNT(reviews.id) AS total_reviews " +
                 "FROM restaurants " + "LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
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
                 "FROM restaurants " + "INNER JOIN bookmarks ON restaurants.id = bookmarks.restaurant_id " + "LEFT JOIN reviews ON restaurants.id = reviews.restaurant_id " +
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
}
