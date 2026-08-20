package com.lab.database.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.lab.database.Database;

public class BookmarkQ {
  
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

  public static boolean addBookmark(int userId, int restaurantId)
  {
    String sql = "INSERT INTO bookmarks (user_id, restaurant_id) VALUES (?, ?)";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);

      return ps.executeUpdate() > 0;
    } catch(Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean removeBookmark(int userId, int restaurantId)
  {
    String sql = "DELETE FROM bookmarks WHERE user_id = ? AND restaurant_id = ?";

    try(Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, userId);
      ps.setInt(2, restaurantId);

      return ps.executeUpdate() > 0;
    } catch(Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
