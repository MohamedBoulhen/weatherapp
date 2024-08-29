package com.anuragroy.Controllers;

import com.anuragroy.Models.ImageHandler;
import com.anuragroy.Models.WeatherManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private WeatherManager weatherManager;
    private String citySet;

    @FXML
    private ImageView img;
    @FXML
    private JFXButton change, set, cancel;
    @FXML
    private JFXTextField cityName, invis;
    @FXML
    private Label city, temperature, day, desc, errors, windSpeed, cloudiness, pressure, humidity;

    // Constructor to set the initial city
    public Controller() {
        this.citySet = "Potsdam".toUpperCase(); // Default city, you might want to update it based on your requirements
    }

    // Event Handler for button clicks
    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent ae) {
        String initialCity = city.getText(); // Stores the last searched city-name

        if (ae.getSource() == change) {
            cityName.setText("");
            bottomSet(true);
            cityName.requestFocus();
        } else if (ae.getSource() == set) {
            setPressed();
        } else if (ae.getSource() == cancel) {
            cityName.setText(initialCity);
            bottomSet(false);
            invis.requestFocus();
        }
    }

    // Method to clear all fields
    private void reset() {
        bottomSet(false);
        day.setText("");
        temperature.setText("");
        desc.setText("");
        windSpeed.setText("");
        cloudiness.setText("");
        pressure.setText("");
        humidity.setText("");
        img.setImage(null);
    }

    // Method to set the new city and update weather
    private void setPressed() {
        if (cityName.getText().trim().isEmpty()) {
            showToast("City Name cannot be blank");
            return;
        }

        try {
            errors.setText("");
            this.citySet = cityName.getText().trim();
            cityName.setText(cityName.getText().trim().toUpperCase());
            weatherManager = new WeatherManager(citySet);
            showWeather();
            bottomSet(false);
            invis.requestFocus();
        } catch (Exception e) {
            city.setText("Error!!");
            city.setTextFill(Color.TOMATO);
            showToast("City with the given name was not found.");
            reset();
            invis.requestFocus();
        }
    }

    // Method to control bottom nodes visibility
    private void bottomSet(boolean isVisible) {
        cityName.setDisable(!isVisible);
        set.setVisible(isVisible);
        change.setVisible(!isVisible);
        cancel.setVisible(isVisible);
    }

    // Method to show error messages with animation
    private void showToast(String message) {
        errors.setText(message);
        errors.setOpacity(1);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), errors);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        fadeIn.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.play();
            pause.setOnFinished(event2 -> {
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), errors);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.play();
            });
        });
    }

    // Method to display weather data and update the scene
    private void showWeather() {
        weatherManager.getWeather();
        city.setText(weatherManager.getCity().toUpperCase());
        temperature.setText(weatherManager.getTemperature() + "°C");
        day.setText(weatherManager.getDay().toUpperCase());
        desc.setText(weatherManager.getDescription().toUpperCase());
        img.setImage(new Image(ImageHandler.getImage(weatherManager.getIcon())));
        windSpeed.setText(weatherManager.getWindSpeed() + " m/s");
        cloudiness.setText(weatherManager.getCloudiness() + "%");
        pressure.setText(weatherManager.getPressure() + " hPa");
        humidity.setText(weatherManager.getHumidity() + "%");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cityName.setText(citySet);
        cityName.setDisable(true);
        set.setVisible(false);
        cancel.setVisible(false);
        errors.setText("");
        weatherManager = new WeatherManager(citySet);
        invis.requestFocus();

        try {
            showWeather();
        } catch (Exception e) {
            city.setText("Error!! - No Internet");
            city.setTextFill(Color.TOMATO);
            showToast("Internet Down. Please Connect");
            reset();
            change.setDisable(true);
            cityName.setText("");
        }

        cityName.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                setPressed();
            }
        });

        // Add shadow effect to the image
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        shadow.setColor(Color.GRAY);
        img.setEffect(shadow);
    }
}
