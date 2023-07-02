package com.manely.ap.project.client;

import com.manely.ap.project.client.controller.Scene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    public static final ExecutorService tasks = Executors.newCachedThreadPool();

    @Override
    public void start(Stage stage) throws IOException {
        Scene.setPrimaryStage(stage);

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("entry.fxml"));
        javafx.scene.Scene scene = new javafx.scene.Scene(fxmlLoader.load());
        stage.setTitle("Twitter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //TODO: poll / video
}