package com.lab.controller.user;

import com.lab.utility.Lib;
import com.lab.database.model.Restaurant;
import com.lab.database.model.Session;
import com.lab.database.query.BookmarkQ;
import com.lab.database.query.RestaurantQ;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class UserRestaurantsRowController {
  @FXML private Text name;
  @FXML private Label address;
  @FXML private Text starsNum;
  @FXML private Text reviewsNum;
  @FXML private Button bookmark;

  private Restaurant restaurant;
  private boolean isBookmarked = false;
  
  public void setRestaurant(Restaurant r)
  {
    restaurant = r;

    name.setText(r.getName());

    String fullAddress = r.getAddress();
    String shortAddress = fullAddress;
    if(fullAddress.contains(",")){
      String[] split = fullAddress.split(",");
      if(split.length >= 2) shortAddress = split[0] + ", " + split[1]; 
    }
    address.setText(shortAddress);

    starsNum.setText(String.format("%.1f", r.getAverageStars()));
    reviewsNum.setText(String.valueOf(r.getReviewsNum()));

    if(Session.getCurrentUser() != null) {
      int userId = Session.getCurrentUser().getId();
      isBookmarked = BookmarkQ.isBookmarked(userId, r.getId());
      updateBookmark();
    } else {
      bookmark.setVisible(false); 
    }
  }

  @FXML public void detailClicked(ActionEvent e)
  {
    System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Detail button clicked");
    UserHomeController.getInstance().openDetails(restaurant);
  }

  @FXML void bookmarkClicked(ActionEvent e)
  {
    if (Session.getCurrentUser() == null) return;

    int userId = Session.getCurrentUser().getId();
    int restId = restaurant.getId();

    if(isBookmarked) {
      if(BookmarkQ.removeBookmark(userId, restId)) {
            isBookmarked = false;
            System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Unbookmarked [" + restaurant.getName() + "]");
        }
      } else {
      if(BookmarkQ.addBookmark(userId, restId)) {
        isBookmarked = true;
        System.out.println("[" + Lib.GREEN + "ACTION" + Lib.RESET + "] Bookmarked [" + restaurant.getName() + "]");
      }
    }

    updateBookmark();
  }

  private void updateBookmark()
  {
    bookmark.getStyleClass().remove("bookmarkButton");
    bookmark.getStyleClass().remove("bookmarkedButton");

    if (isBookmarked) bookmark.getStyleClass().add("bookmarkedButton");
    else bookmark.getStyleClass().add("bookmarkButton");
  }
}
