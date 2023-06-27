package com.manely.ap.project.client.controller;

import javafx.fxml.FXML;

public class EntryController {

    @FXML
    void loginButtonPressed() {
        SceneController.changeScene("login.fxml");
    }

    @FXML
    void signUpButtonPressed() {
        SceneController.changeScene("signup.fxml");
    }

}
