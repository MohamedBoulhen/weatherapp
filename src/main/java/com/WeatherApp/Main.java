package com.WeatherApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/weather.fxml"));

            // Set the stage properties
            primaryStage.setTitle("Weather");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
            primaryStage.setScene(new Scene(root, 1050, 670));
            primaryStage.getScene().getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            // Print stack trace for debugging
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
