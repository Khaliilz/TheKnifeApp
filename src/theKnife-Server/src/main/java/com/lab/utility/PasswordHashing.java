/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.utility;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Questa classe si occupa essenzialmente di hashare la password in chiaro dell'utente durante l'accesso e registrazione.
 * Inoltre fornisce un metodo per il controllo (check) dell'equivalenza tra password in chiaro e hashata.
 */
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
