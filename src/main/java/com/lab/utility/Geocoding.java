package com.lab.utility;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class Geocoding {
  
  public static double[] getCoordinates(String address)
  {
    try{
      String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
      String url = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json&limit=1";

      
      HttpClient client = HttpClient.newHttpClient();

      HttpRequest request = HttpRequest.newBuilder()
                  .uri(URI.create(url))
                  .header("User-Agent", "TheKnifeApp/1.0 (Progetto JavaFX Universitario)")
                  .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      JSONArray jsonArray = new JSONArray(response.body());
            
      if(jsonArray.length() > 0) {
        JSONObject result = jsonArray.getJSONObject(0);
                
        double lat = Double.parseDouble(result.getString("lat"));
        double lon = Double.parseDouble(result.getString("lon"));
                
        return new double[] {lat, lon};
      }
            
      }catch(Exception e) {
        System.out.println("[" + Lib.RED + "ERROR" + Lib.RESET + "] Coordinates not found for [" + address + "]");
        e.printStackTrace();
      }
      
      return null;
    }

}
