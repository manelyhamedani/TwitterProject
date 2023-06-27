package com.manely.ap.project.client;

import com.manely.ap.project.client.controller.SceneController;
import com.sun.net.httpserver.HttpExchange;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    public static final ExecutorService tasks = Executors.newCachedThreadPool();

    @Override
    public void start(Stage stage) throws IOException {
        SceneController.setPrimaryStage(stage);

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Twitter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}