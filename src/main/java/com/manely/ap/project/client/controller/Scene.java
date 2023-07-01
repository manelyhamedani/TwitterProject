package com.manely.ap.project.client.controller;


import com.manely.ap.project.client.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.*;

public class Scene {
    private static Stage primaryStage;
    private static javafx.scene.Scene homeScene = null;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    private Scene() {

    }

    public static void changeScene(String path) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path));
        try {
            javafx.scene.Scene scene = new javafx.scene.Scene(fxmlLoader.load());
            Platform.runLater(() -> {
                primaryStage.setScene(scene);
                primaryStage.show();
                primaryStage.centerOnScreen();
                if (path.equals("home-page.fxml")) {
                    homeScene = scene;
                    HomePage.getInstance().homeButtonPressed();
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void gotoHomePage() {
        Platform.runLater(() -> {
            while (homeScene == null) {

            }
            primaryStage.setScene(homeScene);
            primaryStage.show();
        });
}

}
