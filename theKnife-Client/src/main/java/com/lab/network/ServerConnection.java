/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.lab.interfaces.ServerInterface;
import com.lab.utility.StringColor;

/**
 * Gestisce la connessione di rete lato Client verso il Server tramite il protocollo Java RMI.
 * <p>
 * Questa classe agisce come punto di accesso centralizzato per le comunicazioni distribuite. 
 * Si occupa di localizzare il Registry RMI all'indirizzo host specificato,
 * recuperare il riferimento all'oggetto remoto pubblicato dal server e renderlo disponibile a tutti i controller dell'applicazione per l'invocazione dei metodi di backend.
 * </p>
 */
public class ServerConnection {
  
  private static ServerInterface server;

  /**
   * Tenta di stabilire una connessione con il Registry RMI sul server remoto.
   * <p>
   * Il metodo cerca il registro sulla porta predefinita (1099) dell'host fornito
   * Tenta di eseguire il lookup (ricerca) dell'oggetto remoto registrato con il nome "TheKnifeServer".
   * In caso di successo, salva il riferimento per gli utilizzi futuri.
   * </p>
   * 
   * @param host Indirizzo IP del server.
   * @return true se la connessione al server e' andata a buon fine, false altrimenti.
   */
  public static boolean connect(String host)
  {
    try{
      Registry registry = LocateRegistry.getRegistry(host, 1099);
      server = (ServerInterface) registry.lookup("TheKnifeServer");
      System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] " + host + " connesso al server");
      
      return true;
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Impossibile connettersi al server");
      return false;
    }
  }

  /**
   * Restituisce l'interfaccia remota del server attualmente attiva.
   */
  public static ServerInterface getServer()
  {
    return server;
  }
}
