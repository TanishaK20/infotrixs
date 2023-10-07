
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyConvertor {

    private static final String FAVORITE_CURRENCIES_FILE = "favorite_currencies.txt";

    private static final String API_KEY = "64177821f3e30c8a7e3448402cf76c30";
    private static final String API_URL = "http://api.exchangerate.host";

    private static List<String> favoriteCurrencies = new ArrayList<>();

    public static void main(String[] args) {

        loadFavoriteCurrenciesFromFile();

        if (args.length < 1) {
            System.out.println("Usage: java currency <command>");
            System.exit(1);
        }

        String command = args[0].toLowerCase();

        switch (command) {
            case "add":
                if (args.length != 2) {
                    System.out.println("Usage: java currency add <currencyCode>");
                    System.exit(1);
                }
                addFavoriteCurrency(args[1].toUpperCase());
                break;

            case "view":
                viewFavoriteCurrencies();
                break;

            case "update":
                if (args.length != 4) {
                    System.out.println("Usage: java currency update <fromCurrency> to <toCurrency>");
                    System.exit(1);
                }
                updateFavoriteCurrency(args[1].toUpperCase(), args[3].toUpperCase());
                break;

            case "convert":
                if (args.length != 5) {
                    System.out.println("Usage: java currency convert <amount> <fromCurrency> to <toCurrency>");
                    System.exit(1);
                }
                double amount = Double.parseDouble(args[1]);
                String fromCurrency = args[2].toUpperCase();
                String toCurrency = args[4].toUpperCase();
                convertCurrency(amount, fromCurrency, toCurrency);
                break;

            case "help":
                displayHelp();
                break;

            default:
                System.out.println("Unknown command. Use 'help' for a list of commands.");
                break;
        }

        // Save the updated list to the file
        saveFavoriteCurrenciesToFile();
    }

    // Display available commands
    private static void displayHelp() {
        System.out.println("Available Commands:");
        System.out.println("add <currencyCode>");
        System.out.println("view");
        System.out.println("update <fromCurrency> to <toCurrency>");
        System.out.println("convert <amount> <fromCurrency> to <toCurrency>");
        System.out.println("exchange");
        System.out.println("help");
    }

    private static void loadFavoriteCurrenciesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FAVORITE_CURRENCIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                favoriteCurrencies.add(line.trim().toUpperCase());
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private static void saveFavoriteCurrenciesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVORITE_CURRENCIES_FILE))) {
            for (String currency : favoriteCurrencies) {
                writer.write(currency);
                writer.newLine();
            }
        } catch (IOException e) {
            handleException("An error occurred while saving favorite currencies to file.", e);
        }
    }

    // Add a currency code to the list of favorite currencies
    private static void addFavoriteCurrency(String currencyCode) {
        if (!favoriteCurrencies.contains(currencyCode)) {
            favoriteCurrencies.add(currencyCode);
            System.out.println("Added " + currencyCode + " to your favorite currencies.");
        } else {
            System.out.println(currencyCode + " is already in your favorite currencies.");
        }
    }

    // Update a favorite currency from one to another
    private static void updateFavoriteCurrency(String fromCurrency, String toCurrency) {
        if (favoriteCurrencies.contains(fromCurrency)) {
            if (favoriteCurrencies.contains(toCurrency)) {
                System.out.println(toCurrency + " is already in your favorite currencies.");
            } else {
                favoriteCurrencies.remove(fromCurrency);
                favoriteCurrencies.add(toCurrency);
                System.out.println("Updated favorite currency from " + fromCurrency + " to " + toCurrency);
            }
        } else {
            System.out.println(fromCurrency + " is not in your favorite currencies.");
        }
    }

    // Currency Convertor
    private static void convertCurrency(double amount, String fromCurrency, String toCurrency) {
        if (favoriteCurrencies.contains(fromCurrency) && favoriteCurrencies.contains(toCurrency)) {
            String apiUrl = API_URL + "/convert?from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount
                    + "&access_key=" + API_KEY;

            try {
                JSONObject jsonResponse = fetchCurrencyData(apiUrl);

                if (jsonResponse.has("result")) {
                    double conversionRate = jsonResponse.getDouble("result");
                    System.out.println(conversionRate + " " + toCurrency);
                } else if (jsonResponse.has("error")) {
                    JSONObject error = jsonResponse.getJSONObject("error");
                    if (error.has("type") && error.has("info")) {
                        String errorInfo = error.getString("type");
                        String infoMsg = error.getString("info");
                        System.out.println("Error Type: " + errorInfo);
                        System.out.println("Info: " + infoMsg);
                    } else {
                        System.out.println("API Error: Unknown error");
                    }
                } else {
                    System.out.println("JSON response is missing conversion rate data.");
                }
            } catch (Exception e) {
                handleException("An error occurred while converting currency.", e);
            }
        } else {
            System.out.println("Both " + fromCurrency + " and " + toCurrency + " must be in your favorite currencies.");
        }
    }

    // Fetch currency data from the API
    private static JSONObject fetchCurrencyData(String apiUrl) throws IOException {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return new JSONObject(response.toString());
            }
        } catch (IOException | JSONException e) {
            handleException("An error occurred while fetching currency data.", e);
            return new JSONObject();
        }
    }

  

    // to view favourite Curriencies
    private static void viewFavoriteCurrencies() {
        if (favoriteCurrencies.isEmpty()) {
            System.out.println("You haven't added any favorite currencies yet.");
            return;
        }

        try {
            System.out.println("Your favorite currencies and their exchange rates");
        

            for (String sourceCurrency : favoriteCurrencies) {

                List<String> targetCurrencies = new ArrayList<>(favoriteCurrencies);
                targetCurrencies.remove(sourceCurrency);

                // Create a comma-separated string of target currencies
                String currenciesParam = String.join(",", targetCurrencies);
                String apiUrl = API_URL + "/live?access_key=" + API_KEY + "&currencies=" + currenciesParam + "&source="
                        + sourceCurrency;

                // Fetch currency data from the API
                JSONObject jsonResponse = fetchCurrencyData(apiUrl);

                if (jsonResponse.getBoolean("success")) {
                    System.out.println( sourceCurrency);
                    System.out.println("Its exchange rates - ");

                    JSONObject quotes = jsonResponse.getJSONObject("quotes");

                    for (String targetCurrency : targetCurrencies) {
                        String currencyPair = sourceCurrency + targetCurrency;
                        if (quotes.has(currencyPair)) {
                            double rate = quotes.getDouble(currencyPair);
                            System.out.println(sourceCurrency + "-" + targetCurrency + ": " + rate);
                        }
                    }
                    System.err.println();
                }
                 else {
                    System.out.println( sourceCurrency + " - " + "not a valid curriency");
                }
             
            }
        } catch (Exception e) {
            handleException("An error occurred while fetching currency data.", e);
        }
    }

      // Handle exceptions by printing the message and stack trace
    private static void handleException(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }

}
