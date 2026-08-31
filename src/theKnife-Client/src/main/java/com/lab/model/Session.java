/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.model;

/**
 * Session gestisce la sessione dell'utente corrente.
 * <p>
 * Permette di impostare, rimuovere e ottenere l'utente corrente.
 * </p> 
 */
public class Session {

  private static User currentUser;

  /**
   * Metodo setter.
   * <p>
   * Permette di impostare l'utente corrente della sessione.
   * </p> 
   */
  public static void setCurrentUser(User user)
  {
    currentUser = user;
  }

  /**
   * Metodo getter.
   * <p>
   * Permette di ottenere l'utente corrente della sessione.
   * </p> 
   */
  public static User getCurrentUser()
  {
    return currentUser;
  }

  /**
   * Permette di impostare l'utente corrente della sessione come null indicandone la sua uscita.
   */
  public static void signOut()
  {
    currentUser = null;
  }
}