package com.lab.model;

import java.io.Serializable;

public class Restaurant implements Serializable{
  
  private static final long serialVersionUID = 1L; 

  private int id;
  private String name;
  private String address;
  private String cuisine;
  private String price;
  private double distance;
  private String delivery;
  private String booking;
  private double averageStars;
  private int reviewsNum;

  public Restaurant(int id, String name, String address, String cuisine, String price, double distance, String delivery, String booking, double averageStars, int reviewsNum)
  {
    this.id = id;
    this.name = name;
    this.address = address;
    this.cuisine = cuisine;
    this.price = price;
    this.distance = distance;
    this.delivery = delivery;
    this.booking = booking;
    this.averageStars = averageStars;
    this.reviewsNum = reviewsNum;
  }

  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getAddress()
  {
    return address;
  }

  public String getCuisine()
  {
    return cuisine;
  }

  public String getPrice()
  {
    return price;
  }

  public double getDistance()
  {
    return distance;
  }

  public String getDelivery()
  {
    return delivery;
  }

  public String getBooking()
  {
    return booking;
  }

  public double getAverageStars()
  {
    return averageStars;
  }

  public int getReviewsNum()
  {
    return reviewsNum;
  }
}
