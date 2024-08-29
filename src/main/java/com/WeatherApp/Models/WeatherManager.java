package com.WeatherApp.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeatherManager {
    private final String city;
    private String day;
    private Integer temperature;
    private String icon;
    private String description;
    private String windSpeed;
    private String cloudiness;
    private String pressure;
    private String humidity;

    // Replace with your actual API key
    private static final String API_KEY = "65c95b1c0446a6e71b8175f91e305b27";

    public WeatherManager(String city) {
        this.city = city;
    }

    // Build a String from the read Json file
    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    // Reads and returns the JsonObject
    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }

    // Method to get the weather of the selected city
    public void getWeather() {
        SimpleDateFormat df2 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();

        // Construct the URL with your API key
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&lang=en&units=metric",
                city, API_KEY);

        try {
            JSONObject json = readJsonFromUrl(url);
            JSONObject jsonSpecific;

            // Extract and handle data from the JSON response
            jsonSpecific = json.getJSONObject("main");
            this.temperature = jsonSpecific.getInt("temp");
            this.pressure = Integer.toString(jsonSpecific.getInt("pressure")); // Convert to string
            this.humidity = Integer.toString(jsonSpecific.getInt("humidity")); // Convert to string

            jsonSpecific = json.getJSONObject("wind");
            this.windSpeed = Double.toString(jsonSpecific.getDouble("speed")); // Convert to string

            jsonSpecific = json.getJSONObject("clouds");
            this.cloudiness = Integer.toString(jsonSpecific.getInt("all")); // Convert to string

            c.add(Calendar.DATE, 0);
            this.day = df2.format(c.getTime());

            jsonSpecific = json.getJSONArray("weather").getJSONObject(0);
            this.description = jsonSpecific.getString("description");
            this.icon = jsonSpecific.getString("icon");

        } catch (IOException e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error parsing weather data: " + e.getMessage());
        }
    }

    // Getters for all the private fields
    public String getCity() {
        return city;
    }

    public String getDay() {
        return day;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }
}
