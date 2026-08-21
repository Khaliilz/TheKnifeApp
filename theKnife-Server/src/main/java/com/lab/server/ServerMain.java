package com.lab.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.util.Scanner;

import com.lab.database.Database;
import com.lab.database.PopulateRestaurants;
import com.lab.database.query.RestaurantQ;
import com.lab.utility.StringColor;

public class ServerMain {
  
  public static void main(String[] args)
  {
    System.out.println(StringColor.GREEN + "===== "+ StringColor.RESET + "THE KNIFE - SERVER" + StringColor.GREEN + " =====" + StringColor.RESET);
    
    Scanner scanner = new Scanner(System.in);

    System.out.println("Configurazione del database\n");
    System.out.print("Inserisci l'host del database (localhost:5432): ");
    String host = scanner.nextLine();

    System.out.print("\nInserisci l'username del database (postgres): ");
    String user = scanner.nextLine();

    System.out.print("\nInserisci la password del database: ");
    String password = scanner.nextLine();

    Database.connect(host, user, password);

    try(Connection connection = Database.getConnection()) {
      System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Connesso");

      if(RestaurantQ.isDatabaseEmpty()) {
        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Database vuoto. Importazione dei ristoranti...");
        PopulateRestaurants.importRestaurants("/com/lab/data/dataset.csv");
        System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Database popolato con successo");
      } else System.out.println("[" + StringColor.PURPLE + "DATABASE" + StringColor.RESET + "] Database gia' popolato");

      ServerImpl server = new ServerImpl();
      Registry registry = LocateRegistry.createRegistry(1099);
      registry.rebind("TheKnifeServer", server);

      System.out.println("[" + StringColor.YELLOW + "SERVER" + StringColor.RESET + "] In ascolto ...");
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println("[" + StringColor.RED + "ERROR" + StringColor.RESET + "] Impossibile avviare il server");
    } finally {
      scanner.close();
    }
  }
}
