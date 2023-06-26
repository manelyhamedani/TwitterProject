package com.manely.ap.project.client.controller;


import com.manely.ap.project.client.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    private SceneController() {

    }

    public static void changeScene(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
