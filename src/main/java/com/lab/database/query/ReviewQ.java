package com.lab.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lab.database.Database;
import com.lab.utility.Lib;

public class ReviewQ {
  
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
      System.out.println("[" + Lib.RED + "DATABASE" + Lib.RESET + "] Acquiring reviews failed");
    }

    return list;
  }

  public static String[] getUserReview(int userId, int restaurantId)
  {
    String sql = "SELECT stars, comment, answer FROM reviews WHERE user_id = ? AND restaurant_id = ?";
      
    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      
      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);
      
      ResultSet rs = ps.executeQuery();
            
      if(rs.next()) return new String[] { String.valueOf(rs.getInt("stars")), rs.getString("comment"), rs.getString("answer") };
            
      }catch(Exception e) {
        e.printStackTrace();
      }

      return null;
    }

    public static boolean addReview(int userId, int restaurantId, int stars, String comment)
  {
    String sql = "INSERT INTO reviews (user_id, restaurant_id, stars, comment) VALUES (?, ?, ?, ?)";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);
      ps.setInt(3, stars);
      ps.setString(4, comment);

      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + Lib.RED + "DATABASE" + Lib.RESET + "] Uploading review failed");
      return false;
    }
  }

    public static boolean updateReview(int userId, int restaurantId, int stars, String comment)
    {
      String sql = "UPDATE reviews SET stars = ?, comment = ? WHERE user_id = ? AND restaurant_id = ?";
      
      try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
        ps.setInt(1, stars);
        ps.setString(2, comment);
        ps.setInt(3, userId);
        ps.setInt(4, restaurantId);
            
        return ps.executeUpdate() > 0;
      }catch(Exception e) {
        e.printStackTrace();
        System.out.println("[" + Lib.RED + "DATABASE" + Lib.RESET + "] Updating review failed");
        return false;
      }
    }

    public static boolean removeReview(int userId, int restaurantId)
  {
    String sql = "DELETE FROM reviews WHERE user_id = ? AND restaurant_id = ?";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);

      return ps.executeUpdate() > 0;
    }catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + Lib.RED + "DATABASE" + Lib.RESET + "] Removing review failed");
      return false;
    }
  }
}
