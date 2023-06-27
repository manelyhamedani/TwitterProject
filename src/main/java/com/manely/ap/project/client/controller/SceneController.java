package com.manely.ap.project.client.controller;


import com.manely.ap.project.client.DataResource;
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

    public static Scene getMainScene() throws IOException {
        String path;

        File usrFile = new File(DataResource.user);
        if (!usrFile.exists() || usrFile.length() == 0) {
            new ObjectOutputStream(new FileOutputStream(usrFile));
            path = "entry.fxml";
        }
        else {
            User usrObj;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usrFile))) {
                usrObj = (User) ois.readObject();
                String jwt = usrObj.getJwt();
                HashMap<String, String> query = new HashMap<>();
                query.put("jwt", jwt);

                ResponseCallback<Object> responseCallback =  new ResponseCallback<>() {
                    @Override
                    public void run() {
                    }
                };

                HttpCall.get(API.CONNECT, query, Object.class, responseCallback);

                while (responseCallback.getResponse() == null) {

                }

                if (responseCallback.getResponse().isSuccess()) {
                    path = "home-page.fxml";
                }
                else {
                    path = "entry.fxml";
                    usrFile.delete();
                }
            }
            catch (Exception e) {
                usrFile.delete();
                path = "entry.fxml";
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(path));
        return new Scene(fxmlLoader.load());
    }
}
