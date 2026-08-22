package com.lab.server;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.util.Scanner;

import com.lab.database.Database;
import com.lab.database.DatabaseInit;
import com.lab.database.Populate;
import com.lab.database.query.RestaurantQ;
import com.lab.utility.StringColor;

public class ServerMain {
  
  public static void main(String[] args)
  {
    System.out.println(StringColor.GREEN + "===== "+ StringColor.RESET + "THE KNIFE - SERVER" + StringColor.GREEN + " =====" + StringColor.RESET);
    
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

      Database.connect(host, user, password);
      
      try(Connection connection = Database.getConnection()) {

        if(connection == null) throw new Exception("Credienziali errate o DB non raggiungibile");
        
        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Connesso");

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
                  System.out.println("[" + StringColor.RED + "COMANDO SCONOSCIUTO" + StringColor.RESET + "] Scrivi 'stop' oppure 'exit' per chiudere il server");
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
