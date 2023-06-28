package com.manely.ap.project.client.controller;

import com.manely.ap.project.client.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.manely.ap.project.client.util.ButtonUtility.setColorButtonImage;


public class HomePage {
    private Image homeImage;
    private Image profileImage;
    private Image searchImage;
    private Image settingsImage;
    private Image coloredHomeImage;
    private Image coloredProfileImage;
    private Image coloredSearchImage;
    private Image coloredSettingsImage;

    @FXML
    private BorderPane root;

    @FXML
    private Button homeButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button settingsButton;

    public void initialize() throws IOException {
        setButtonImages();

        coloredHomeImage = setColorButtonImage(homeButton);
        coloredProfileImage = setColorButtonImage(profileButton);
        coloredSearchImage = setColorButtonImage(searchButton);
        coloredSettingsImage = setColorButtonImage(settingsButton);

        homeButtonPressed();
    }

    private void setButtonImages() throws MalformedURLException {
        URL home = new URL("file:/Users/melody/Downloads/home-button.png");
        URL search = new URL("file:/Users/melody/Downloads/search-button.jpg");
        URL profile = new URL("file:/Users/melody/Downloads/profile-button.png");
        URL settings = new URL("file:/Users/melody/Downloads/settings-button.png");

        ImageView homeImageView = new ImageView();
        homeImageView.setFitHeight(26);
        homeImageView.setFitWidth(26);

        ImageView searchImageView = new ImageView();
        searchImageView.setFitHeight(26);
        searchImageView.setFitWidth(26);

        ImageView profileImageView = new ImageView();
        profileImageView.setFitWidth(26);
        profileImageView.setFitHeight(26);

        ImageView settingsImageView = new ImageView();
        settingsImageView.setFitWidth(29);
        settingsImageView.setFitHeight(29);

        homeImage = new Image(home.toString());
        ((ImageView) homeButton.getGraphic()).setImage(homeImage);

        searchImage = new Image(search.toString());
        ((ImageView) searchButton.getGraphic()).setImage(searchImage);

        profileImage = new Image(profile.toString());
        ((ImageView) profileButton.getGraphic()).setImage(profileImage);

        settingsImage = new Image(settings.toString());
        ((ImageView) settingsButton.getGraphic()).setImage(settingsImage);
    }



    private void resetButtonImages() {
        ((ImageView) homeButton.getGraphic()).setImage(homeImage);
        ((ImageView) profileButton.getGraphic()).setImage(profileImage);
        ((ImageView) searchButton.getGraphic()).setImage(searchImage);
        ((ImageView) settingsButton.getGraphic()).setImage(settingsImage);
    }


    @FXML
    void homeButtonPressed() throws IOException {
        resetButtonImages();
        ((ImageView) homeButton.getGraphic()).setImage(coloredHomeImage);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("timeline.fxml"));
        Timeline timeline = new Timeline();
        fxmlLoader.setRoot(timeline);
        fxmlLoader.load();
        root.setCenter(timeline);
    }

    @FXML
    void profileButtonPressed() {
        resetButtonImages();
        ((ImageView) profileButton.getGraphic()).setImage(coloredProfileImage);

    }

    @FXML
    void searchButtonPressed() {
        resetButtonImages();
        ((ImageView) searchButton.getGraphic()).setImage(coloredSearchImage);

    }

    @FXML
    void settingsButtonPressed() {
        resetButtonImages();
        ((ImageView) settingsButton.getGraphic()).setImage(coloredSettingsImage);

    }

}
