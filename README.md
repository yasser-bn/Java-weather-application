
# Weather Information App

## Overview

The Weather Information App is a Java-based desktop application that allows users to search for and view current weather information and an 8-day forecast for any location. The app provides a user-friendly interface where users can enter a location, select temperature units (Celsius or Fahrenheit), and view detailed weather information, including temperature, humidity, wind speed, and weather conditions.

## Features

- **Location-Based Weather Search:** Enter any location (city name) to fetch the current weather and an 8-day forecast.
- **Temperature Unit Selection:** Choose between Celsius and Fahrenheit for displaying temperature.
- **Weather Icon Display:** The app fetches and displays weather icons corresponding to the current weather conditions.
- **Search History:** A list of previous searches is maintained, allowing users to quickly revisit past queries.
- **8-Day Forecast:** Provides an 8-day weather forecast with day-specific temperatures and conditions.

## Requirements

- Java Development Kit (JDK) 8 or higher
- Internet connection (for fetching weather data)
- OpenWeatherMap API Key

## Installation and Setup

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-repository/weather-information-app.git
   cd weather-information-app
   ```

2. **Configure the API Key:**
   - The app uses the OpenWeatherMap API to fetch weather data. Replace the placeholder API key in the `WeatherAPIService` class with your own API key from OpenWeatherMap:
     ```java
     private static final String API_KEY = "your_openweathermap_api_key";
     ```

3. **Compile and Run the Application:**
   - Use your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse) or command line to compile and run the application.
   - From the command line:
     ```bash
     javac WeatherApp.java WeatherAPIService.java
     java WeatherApp
     ```

## Usage

1. **Launch the App:**
   - Upon launching, you will see a window titled "Weather App."

2. **Search for Weather Information:**
   - Enter the name of the city in the text field at the top.
   - Click the "Search" button to retrieve the weather information for the entered location.
   - The current weather conditions and an 8-day forecast will be displayed.

3. **Temperature Unit Selection:**
   - Use the drop-down menu at the bottom of the app to switch between Celsius and Fahrenheit.

4. **View Search History:**
   - The app keeps a list of your previous searches. Click on any item in the history list to reload the weather information for that location.

## Implementation Details

- **GUI Design:**
  - The graphical user interface (GUI) is built using Java Swing. The main panel uses a `GridBagLayout` to arrange the components.
  - The background is a gradient from dark blue to light blue, providing a visually appealing interface.
  - The "Search" button is highlighted in yellow to draw attention.

- **Weather Data Fetching:**
  - The app uses the OpenWeatherMap API to fetch current weather data (`/weather` endpoint) and the 8-day forecast (`/forecast` endpoint).
  - Data is fetched in JSON format and parsed using the `org.json` library.

- **Error Handling:**
  - If the app fails to fetch weather data (e.g., due to an invalid location or network issue), an error message is displayed using a `JOptionPane`.

- **Weather Forecast Sorting:**
  - The 8-day forecast data is sorted and displayed in chronological order. The app groups forecasts by day and displays the day name (e.g., "Mon," "Tue") along with the weather details.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.
