package com.lab.utility;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashing {

  public static String hashPassword(String plainPassword)
  {
    return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
  }

  public static boolean checkPassword(String password, String hashedPassword)
  {
    try{
      return BCrypt.checkpw(password, hashedPassword);
    }catch(IllegalArgumentException e) {
      return false;
    }
  }
}
