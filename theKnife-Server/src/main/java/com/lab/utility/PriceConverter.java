package com.lab.utility;

public class PriceConverter {
  
  public static String priceToSymbols(String priceRange)
  {
    if(priceRange == null) return null;

    switch (priceRange) {
      case "10-20": return "€";
      case "30-40": return "€€";
      case "50-60": return "€€€";
      case "70-80": return "€€€€";
      default: return priceRange; 
    }
  }

  public static String symbolsToPrice(String symbols)
  {
    if(symbols == null || symbols.trim().isEmpty()) return null;
    char value = symbols.charAt(0);
    switch (symbols.trim().length()) {
        case 1: return "10-20" + value;
        case 2: return "30-40" + value;
        case 3: return "50-60" + value;
        case 4: return "70-80" + value;
        default: return "10-20" + value; 
    }
  }
}
