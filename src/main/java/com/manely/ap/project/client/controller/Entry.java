package com.manely.ap.project.client.controller;

import javafx.fxml.FXML;

public class Entry {

    @FXML
    void loginButtonPressed() {
        Scene.changeScene("login.fxml");
    }

    @FXML
    void signUpButtonPressed() {
        Scene.changeScene("signup.fxml");
    }

}
