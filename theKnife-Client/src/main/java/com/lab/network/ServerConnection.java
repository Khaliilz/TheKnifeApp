package com.lab.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.lab.interfaces.ServerInterface;
import com.lab.utility.StringColor;

public class ServerConnection {
  
  private static ServerInterface server;

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

  public static ServerInterface getServer()
  {
    return server;
  }
}
