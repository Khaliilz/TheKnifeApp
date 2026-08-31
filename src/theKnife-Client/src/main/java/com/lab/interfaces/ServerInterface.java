/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

import com.lab.model.Restaurant;
import com.lab.model.User;

/**
 * Interfaccia del server, contiene tutti i metodi resi pubblici dal server.
 */
public interface ServerInterface extends Remote {
  
  public User signin(String username, String password) throws RemoteException;
  public boolean signup(String name, String surname, Date birthDate, String address, String username, String plainPassword, String role) throws RemoteException;
  public void signout(int userId) throws RemoteException;

  public List<Restaurant> getNearestRestaurants(double lat, double lon) throws RemoteException;
  public List<Restaurant> getBookmarkedRestaurants(int userId, double lat, double lon) throws RemoteException;
  public List<Restaurant> getReviewedRestaurants(int userId, double lat, double lon) throws RemoteException;
  public List<Restaurant> getSerachedRestaurants(String place, String cuisine, String price, String delivery, String booking, String stars, int offset, double lat, double lon) throws RemoteException;
  public List<Restaurant> getRestaurantsByOwner(int ownerId) throws RemoteException;
  public boolean addRestaurant(String name, String address, String cuisine, String price, String delivery, String booking, double lat, double lon, int ownerId) throws RemoteException;
  public boolean removeRestaurant(int restaurantId) throws RemoteException;

  public List<String[]> getRestaurantReviews(int restaurantId) throws RemoteException;
  public String[] getUserReview(int userId, int restaurantId) throws RemoteException;
  public boolean addReview(int userId, int restaurantId, int stars, String comment) throws RemoteException;
  public boolean updateReview(int userId, int restaurantId, int stars, String comment) throws RemoteException;
  public boolean removeReview(int userId, int restaurantId) throws RemoteException;
  public List<String[]> getRestaurateurReviews(int restaurantId) throws RemoteException;
  public boolean saveReviewAnswer(int userId, int restaurantId, String answer) throws RemoteException;

  public boolean isBookmarked(int userId, int restaurantId) throws RemoteException;
  public boolean addBookmark(int userId, int restaurantId) throws RemoteException;
  public boolean removeBookmark(int userId, int restaurantId) throws RemoteException;
}
