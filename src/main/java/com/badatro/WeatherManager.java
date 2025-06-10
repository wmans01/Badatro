package com.badatro;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Manages weather data and score multipliers based on weather conditions and time of day.
 * Fetches weather data from WeatherAPI and calculates score multipliers.
 */
public class WeatherManager {
    private static final String API_KEY = "8b50b1c33a6b48e992e00611251006"; // Replace with your WeatherAPI key
    private static final String BASE_URL = "http://api.weatherapi.com/v1";
    private String location;
    private final OkHttpClient client;
    private JsonObject currentWeather;
    private LocalDateTime lastUpdate;

    /**
     * Creates a new WeatherManager instance.
     * Initializes the HTTP client for weather API requests.
     */
    public WeatherManager() {
        this.client = new OkHttpClient();
        this.lastUpdate = LocalDateTime.MIN;
    }

    /**
     * Sets the location for weather data and updates the weather information.
     *
     * @param location The location to get weather data for (zip code or city+state)
     */
    public void setLocation(String location) {
        this.location = location;
        updateWeather();
    }

    /**
     * Gets the current location being used for weather data.
     *
     * @return The current location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Updates the weather data by making an API call to WeatherAPI.
     * Stores the current weather conditions and updates the last update timestamp.
     */
    public void updateWeather() {
        if (location == null || location.isEmpty()) {
            return;
        }

        try {
            String url = BASE_URL + "/current.json?key=" + API_KEY + "&q=" + location + "&aqi=no";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response " + response);
                }

                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                currentWeather = jsonResponse.getAsJsonObject("current");
                lastUpdate = LocalDateTime.now();
            }
        } catch (IOException e) {
            e.printStackTrace();
            currentWeather = null;
        }
    }

    /**
     * Gets the current temperature in Fahrenheit.
     *
     * @return The current temperature, or 70.0 if weather data is unavailable
     */
    public double getTemperature() {
        if (currentWeather == null) {
            updateWeather();
        }
        return currentWeather != null ? currentWeather.get("temp_f").getAsDouble() : 70.0;
    }

    /**
     * Gets the current weather condition description.
     *
     * @return The weather condition, or "Unknown" if weather data is unavailable
     */
    public String getWeatherCondition() {
        if (currentWeather == null) {
            updateWeather();
        }
        if (currentWeather != null) {
            return currentWeather.getAsJsonObject("condition").get("text").getAsString();
        }
        return "Unknown";
    }

    /**
     * Calculates the score multiplier based on weather conditions and time of day.
     * Weather multipliers:
     * - Sunny/Clear: 1.2x
     * - Partly Cloudy: 1.1x
     * - Cloudy/Overcast: 0.9x
     * - Rain/Drizzle: 0.8x
     * - Thunder/Storm: 0.7x
     * - Snow/Sleet: 0.6x
     * 
     * Time multipliers:
     * - Early Morning (5-7): 1.3x
     * - Morning (7-9): 1.2x
     * - Evening (17-19): 1.2x
     * - Night (19-21): 1.3x
     *
     * @return The calculated score multiplier
     */
    public double getScoreMultiplier() {
        if (currentWeather == null) {
            updateWeather();
        }

        double weatherMultiplier = 1.0;
        double timeMultiplier = 1.0;

        // Weather multipliers
        if (currentWeather != null) {
            String condition = getWeatherCondition().toLowerCase();
            if (condition.contains("sunny") || condition.contains("clear")) {
                weatherMultiplier = 1.2;
            } else if (condition.contains("partly cloudy")) {
                weatherMultiplier = 1.1;
            } else if (condition.contains("cloudy") || condition.contains("overcast")) {
                weatherMultiplier = 0.9;
            } else if (condition.contains("rain") || condition.contains("drizzle")) {
                weatherMultiplier = 0.8;
            } else if (condition.contains("thunder") || condition.contains("storm")) {
                weatherMultiplier = 0.7;
            } else if (condition.contains("snow") || condition.contains("sleet")) {
                weatherMultiplier = 0.6;
            }
        }

        // Time of day multipliers
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.of(5, 0)) && now.isBefore(LocalTime.of(7, 0))) {
            timeMultiplier = 1.3; // Early morning
        } else if (now.isAfter(LocalTime.of(7, 0)) && now.isBefore(LocalTime.of(9, 0))) {
            timeMultiplier = 1.2; // Morning
        } else if (now.isAfter(LocalTime.of(17, 0)) && now.isBefore(LocalTime.of(19, 0))) {
            timeMultiplier = 1.2; // Evening
        } else if (now.isAfter(LocalTime.of(19, 0)) && now.isBefore(LocalTime.of(21, 0))) {
            timeMultiplier = 1.3; // Night
        }

        return weatherMultiplier * timeMultiplier;
    }

    /**
     * Gets a formatted string containing the current weather information.
     *
     * @return A string containing temperature and weather condition
     */
    public String getWeatherInfo() {
        if (currentWeather == null) {
            updateWeather();
        }
        if (currentWeather != null) {
            return String.format("Temperature: %.1f°F, Condition: %s", 
                getTemperature(), getWeatherCondition());
        }
        return "Weather information unavailable";
    }
} 