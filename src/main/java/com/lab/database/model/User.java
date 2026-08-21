package com.lab.database.model;

public class User {
  private int id;
  private String username;
  private String role;
  private String address;
  private double latitude;
  private double longitude;

  public User(int id, String username, String address, double latitutde, double longitude, String role) {
    this.id = id;
    this.username = username;
    this.address = address;
    this.latitude = latitutde;
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
