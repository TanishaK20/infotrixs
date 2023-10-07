# infotrixs
Internship Task -
# Currency Converter

Currency Converter is a command-line tool built in Java that allows users to manage their favorite currencies, view exchange rates, and convert currency amounts with ease.This tool leverages real-time exchange rate data from the ExchangeRate API and the Conversion API, allowing users to add favorite currencies, view exchange rates, update currency preferences, and perform seamless currency conversions.

## Prerequisites
- **External JAR dependency for JSON handling**: You'll need an external JAR file for JSON handling. Replace `path/to/external.jar` with the actual path to your external JAR file.

## Features

- **Add Favorite Currencies:** You can add your favorite currencies to the list for quick access.

- **View Exchange Rates:** Get up-to-date exchange rates for your favorite currencies.
  
- **Update Favorite Currencies:** You can update your favorite currencies .

- **Convert Currency:** Effortlessly convert an amount from one currency to another within your list of favorite currencies.

## Usage

After compiling the project, you can use the Currency Converter by running the Java application.
 Here's the general command structure:

 `java -cp .:path/to/external.jar CurrencyConverter <command> [args]`

 Replace `path/to/external.jar` with the actual path to your external JAR file.
 Replace <command> with one of the available commands and `[args]` with the corresponding arguments


Commands :

- `add <currencyCode>`: Add a currency code to your list of favorite currencies.
- `view`: View your list of favorite currencies.
- `update <fromCurrency> to <toCurrency>`: Update a favorite currency from one code to another.
- `convert <amount> <fromCurrency> to <toCurrency>`: Convert an amount from one currency to another.
- `help`: Display available commands.

Example:
 one example to use the Currency Converter:

- Add a favorite currency:
java -cp .:path/to/external.jar CurrencyConverter add USD
(Replace `path/to/external.jar` with the actual path to your external JAR file.)


## Dependencies

This project uses the following external JAR dependencies:

- `json.jar`: A JSON parsing library for handling currency exchange data.

