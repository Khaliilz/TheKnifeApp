/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.model;

import java.io.Serializable;

/**
 * Oggetto seriale rappresentante l'utente.
 * Contiene i metodi getter per ottenere le informazioni specifiche relative ad un utente in particolare.
 */
public class User implements Serializable{

  private static final long serialVersionUID = 1L;

  private int id;
  private String username;
  private String role;
  private String address;
  private double latitude;
  private double longitude;

  public User(int id, String username, String address, double latitude, double longitude, String role) {
    this.id = id;
    this.username = username;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
    this.role = role;
  }

  public int getId()
  { 
    return id;
  }

  public String getUsername()
  { 
    return username;
  }

  public String getAddress()
  {
    return address;
  }

  public double getLatitude()
  {
    return latitude;
  }

  public double getLongitude()
  {
    return longitude;
  }

  public String getRole()
  { 
    return role;
  }
}
