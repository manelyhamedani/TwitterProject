package com.manely.ap.project.client.controller;


import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.ResponseCallback;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.User;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;

public class SceneController {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    private SceneController() {

    }

    public static void changeScene(String path) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Platform.runLater(() -> {
                primaryStage.setScene(scene);
                primaryStage.show();
            });
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
