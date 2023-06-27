package com.manely.ap.project.client.controller;

import com.google.gson.Gson;
import com.manely.ap.project.client.DataResource;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.ResponseCallback;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class loginController {

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private VBox root;

    public void initialize() {
        Platform.runLater(() -> root.requestFocus());
    }

    @FXML
    void loginButtonPressed() {
        errorLabel.setText("");

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (username.isBlank() || password.isBlank()) {
            errorLabel.setText("Fill the fields!");
        }
        else {
            Map<String, String> query = new HashMap<>();
            query.put("username", username);
            query.put("password", password);

            HttpCall.get(API.LOGIN, query, User.class,
                    new ResponseCallback<>() {
                        @Override
                        public void run() {
                            if (getResponse().isSuccess()) {
                                try {
                                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DataResource.user));
                                    oos.writeObject(getResponse().getContent());
                                    SceneController.changeScene("home-page.fxml");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    System.exit(1);
                                }
                            } else {
                                Platform.runLater(() -> {
                                    usernameTextField.clear();
                                    passwordTextField.clear();
                                    switch (getResponse().getStatus()) {
                                        case 401 -> errorLabel.setText("User does not exist!");
                                        case 403 -> errorLabel.setText("Wrong password!");
                                        default -> errorLabel.setText("Internal error!");
                                    }
                                });
                            }
                        }
                    });
        }
    }

}
