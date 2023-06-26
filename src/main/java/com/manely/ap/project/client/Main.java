package com.manely.ap.project.client;

import com.manely.ap.project.client.controller.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main extends Application {
    public static final BlockingQueue<Runnable> mainThreadQueue = new LinkedBlockingQueue<>();

    @Override
    public void start(Stage stage) throws IOException {
        SceneController.setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("entry.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Twitter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws InterruptedException {
        mainThreadQueue.put(Main::start);

        while (true) {
            mainThreadQueue.take().run();
        }
    }

    public static void start() {
        launch();
    }
}