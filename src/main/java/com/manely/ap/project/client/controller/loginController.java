package com.manely.ap.project.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
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
    void loginButtonPressed() throws IOException {
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

//            HttpCall.get(API.LOGIN, query, User.class, );

            SceneController.changeScene("home-page.fxml");
        }
    }

}
