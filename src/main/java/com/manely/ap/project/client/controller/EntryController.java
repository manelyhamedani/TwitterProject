package com.manely.ap.project.client.controller;

import javafx.fxml.FXML;

import java.io.IOException;

public class EntryController {

    @FXML
    void loginButtonPressed() throws IOException {
        SceneController.changeScene("login.fxml");
    }

    @FXML
    void signUpButtonPressed() throws IOException {
        SceneController.changeScene("signup.fxml");
    }

}
