/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.sql.Date;
import java.util.List;

import com.lab.model.Restaurant;
import com.lab.model.User;
import com.lab.database.query.BookmarkQ;
import com.lab.database.query.RestaurantQ;
import com.lab.database.query.ReviewQ;
import com.lab.database.query.UserQ;
import com.lab.interfaces.ServerInterface;
import com.lab.utility.StringColor;

/**
 * Rappresenta l'implementazione dei metodi resi pubblici dal server.
 * Per questioni di separazione del codice e isolamento dei compiti, le implementazioni vere e proprie si trovano nelle seguenti classi:
 * {@link UserQ}
 * {@link RestaurantQ}
 * {@link BookmarkQ}
 * {@link ReviewQ}
 */
public class ServerImpl extends UnicastRemoteObject implements ServerInterface {

  public ServerImpl() throws RemoteException
  {
    super();
  }

  @Override
  public User signin(String username, String password) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta di accesso per l'utente: " + username);
    return UserQ.signin(username, password);
  }

  @Override
  public boolean signup(String name, String surname, Date birthDate, String address, String username, String plainPassword, String role) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta di registrazione" + username);
    return UserQ.signup(name, surname, birthDate, address, username, plainPassword, role);
  }

  @Override
  public void signout(int userId)
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta di uscita");
  }


  @Override
  public List<Restaurant> getNearestRestaurants(double lat, double lon) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta ristoranti vicini");
    return RestaurantQ.getNearestRestaurants(lat, lon);
  }

  @Override
  public List<Restaurant> getBookmarkedRestaurants(int userId, double lat, double lon) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta ristoranti preferiti");
    return RestaurantQ.getBookmarkedRestaurants(userId, lat, lon);
  }

  @Override
  public List<Restaurant> getReviewedRestaurants(int userId, double lat, double lon) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta ristoranti recensiti");
    return RestaurantQ.getReviewedRestaurants(userId, lat, lon);
  }

  @Override
  public List<Restaurant> getSerachedRestaurants(String place, String cuisine, String price, String delivery, String booking, String stars, int offset, double lat, double lon) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta ristoranti ricercati");
    return RestaurantQ.getSerachedRestaurants(place, cuisine, price, delivery, booking, stars, offset, lat, lon);
  }

  @Override
  public List<Restaurant> getRestaurantsByOwner(int ownerId) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta ristoranti propri");
    return RestaurantQ.getRestaurantsByOwner(ownerId);
  }

  @Override
  public boolean addRestaurant(String name, String address, String cuisine, String price, String delivery, String booking, double lat, double lon, int ownerId) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta aggiunta nuovo ristorante");
    return RestaurantQ.addRestaurant(name, address, cuisine, price, delivery, booking, lat, lon, ownerId);
  }

  @Override  
  public boolean removeRestaurant(int restaurantId) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta rimozione ristorante");
    return RestaurantQ.removeRestaurant(restaurantId);
  }


  @Override
  public List<String[]> getRestaurantReviews(int restaurantId) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta recensioni ristorante proprio");
    return ReviewQ.getRestaurantReviews(restaurantId);
  }

  @Override 
  public String[] getUserReview(int userId, int restaurantId) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta recensione a ristorante");
    return ReviewQ.getUserReview(userId, restaurantId);
  }

  @Override
  public boolean addReview(int userId, int restaurantId, int stars, String comment) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta aggiunta recensione");
    return ReviewQ.addReview(userId, restaurantId, stars, comment);
  }

  @Override
  public boolean updateReview(int userId, int restaurantId, int stars, String comment) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta aggiornamento recensione");
    return ReviewQ.updateReview(userId, restaurantId, stars, comment);
  }

  @Override
  public boolean removeReview(int userId, int restaurantId) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta rimozione recensione");
    return ReviewQ.removeReview(userId, restaurantId);
  }

  @Override
  public List<String[]> getRestaurateurReviews(int restaurantId) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta recensioni ristorante");
    return ReviewQ.getRestaurateurReviews(restaurantId);
  }

  @Override
  public boolean saveReviewAnswer(int userId, int restaurantId, String answer) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta salvataggio risposta");
    return ReviewQ.saveReviewAnswer(userId, restaurantId, answer);
  }



  @Override
  public boolean isBookmarked(int userId, int restaurantId) throws RemoteException
  {
    return BookmarkQ.isBookmarked(userId, restaurantId);
  }

  @Override
  public boolean addBookmark(int userId, int restaurantId) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta aggiunta ristorante ai preferiti");
    return BookmarkQ.addBookmark(userId, restaurantId);
  }

  @Override
  public boolean removeBookmark(int userId, int restaurantId) throws RemoteException
  {
    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + getClientIP() + ": Richiesta rimozione ristorante dai preferiti");
    return BookmarkQ.removeBookmark(userId, restaurantId);
  }


  private String getClientIP()
  {
    try {
      return RemoteServer.getClientHost();
    } catch(ServerNotActiveException e) {
      return "Sconosciuto";
    }
  }
}
