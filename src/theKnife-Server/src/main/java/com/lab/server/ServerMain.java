/**
 * @author Devi Atti 754536  VA
 * @author Zribi Khalil 758699 VA
 */
package com.lab.server;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;

import com.lab.database.Database;
import com.lab.database.DatabaseInit;
import com.lab.database.Populate;
import com.lab.database.query.RestaurantQ;
import com.lab.utility.StringColor;

/**
 * Classe principale di avvio per il Server dell'applicazione TheKnife.
 * <p>
 * Questa classe gestisce l'intero ciclo di vita del server.
 * Si occupa di richiedere  all'amministratore le credenziali e l'host per l'accesso al database PostgreSQL al momento del lancio.
 * Successivamente si occupa di inizializzare la persistenza, eventualmente popolando il database, e pubblica l'oggetto remoto per abilitare  RMI, restando in attesa delle connessioni client.
 * </p>
 */
public class ServerMain {
  
  /**
   * Metodo di ingresso principale che avvia e configura il server TheKnife.
   * Il flusso di esecuzione si divide in tre fasi principali:
   * <ol>
   *  <li><b>Configurazione Database:</b> Tramite CLI, interroga l'utente per ottenere host, username, password e preferenza di formattazione del database.</li>
   *  <li><b>Inizializzazione Dati:</b> Connesso al DBMS, avvia la rigenerazione dello schema relazionale (se richiesta) e l'importazione del dataset (ristoranti e utenti base) qualora il database risulti vuoto.</li>
   *  <li><b>Avvio RMI:</b> Crea un Registry RMI locale sulla porta 1099 e associa l'implementazione dei servizi  al nome "TheKnifeServer".</li>
   * </ol>
   * Infine, un thread parallelo rimane in ascolto sull'input di sistema per permettere uno spegnimento tramite i comandi testuali: stop o exit, deregistrando l'oggetto remoto in modo sicuro.
   */
  public static void main(String[] args)
  {
    System.out.println("\n" + StringColor.GREEN + "===== "+ StringColor.RESET + "THE KNIFE - SERVER" + StringColor.GREEN + " =====" + StringColor.RESET + "\n");
    
    Scanner scanner = new Scanner(System.in);
    Boolean success;

    System.out.println("Configurazione del database");

    do{
      success = true;
      System.out.print("\nInserisci l'host del database (localhost:5432): ");
      String host = scanner.nextLine();
      if(host.isEmpty()) host = "localhost:5432";

      System.out.print("\nInserisci l'username del database (postgres): ");
      String user = scanner.nextLine();
      if(user.isEmpty()) user = "postgres";

      System.out.print("\nInserisci la password del database: ");
      String password = scanner.nextLine();

      System.out.print("\nVuoi formattare il database prima di iniziare? (S/N): ");
      String resetChoice = scanner.nextLine().trim();
      if(resetChoice.isEmpty()) resetChoice = "N";

      Database.connect(host, user, password);
      
      try(Connection connection = Database.getConnection()) {

        if(connection == null) throw new Exception("Credienziali errate o DB non raggiungibile");
        
        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Connesso");

        String sql = "DROP TABLE IF EXISTS reviews CASCADE;" +
                     "DROP TABLE IF EXISTS bookmarks CASCADE;" +
                     "DROP TABLE IF EXISTS restaurants CASCADE;" +
                     "DROP TABLE IF EXISTS users CASCADE;";
        if(resetChoice.equalsIgnoreCase("S")) {
          try(Statement s = connection.createStatement()) {
            s.execute(sql);
            System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Vecchie tabelle eliminate");
          } catch(Exception e) {
            System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Errore durante il reset del DB");
          }
        }

        DatabaseInit.initialize();

        if(RestaurantQ.isDatabaseEmpty()) {
          System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Database vuoto. Importazione dei ristoranti...");
          Populate.restaurants("/com/lab/data/dataset.csv");
          Populate.users();
          System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Database popolato con successo");
        } else System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Database gia' popolato");

        String myIP = "localhost";
        try{
          myIP = InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e) {
          System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Impossibile determinare l'ip");
          System.out.println("[" + StringColor.GREEN + "AZIONE" + StringColor.RESET + "] Utilzzo localhost");
        }
        System.setProperty("java.rmi.server.hostname", myIP);
        System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Rilevato il seguente IP: " + myIP);

        ServerImpl server = new ServerImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("TheKnifeServer", server);

        System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] In ascolto ...");
        Thread consoleThread = new Thread(() -> {
          Scanner cmdScanner = new Scanner(System.in);
            while(true) {
              String command = cmdScanner.nextLine().trim();
              if(command.equalsIgnoreCase("stop") || command.equalsIgnoreCase("exit")) {
                System.out.println("\n[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Spegnimento in corso...");
                  try {
                    registry.unbind("TheKnifeServer");
                    UnicastRemoteObject.unexportObject(server, true);
                    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] RMI Registry disconnesso");
                    
                    System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] Server chiuso correttamente");
                    scanner.close();
                    cmdScanner.close();
                    System.exit(0);
                  } catch(Exception ex) {
                    System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Errore durante la chiusura del server");
                    System.exit(1);
                  }
                } else if(!command.isEmpty()) {
                  System.out.println("[" + StringColor.RED + "ERRORE" + StringColor.RESET + "] Scrivi 'stop' oppure 'exit' per chiudere il server");
              }
          }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();
      } catch(Exception e) {
        System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Impossibile avviare il server");
        success = false;
      }
    }while(!success);
  }
}
