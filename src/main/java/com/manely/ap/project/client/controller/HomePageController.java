package com.manely.ap.project.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.net.MalformedURLException;
import java.net.URL;


public class HomePageController {

    @FXML
    private Button homeButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button settingsButton;

    public void initialize() throws MalformedURLException {
        setButtonImages();
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

        homeImageView.setImage(new Image(home.toString()));
        homeButton.setGraphic(homeImageView);

        searchImageView.setImage(new Image(search.toString()));
        searchButton.setGraphic(searchImageView);

        profileImageView.setImage(new Image(profile.toString()));
        profileButton.setGraphic(profileImageView);

        settingsImageView.setImage(new Image(settings.toString()));
        settingsButton.setGraphic(settingsImageView);
    }

    private void reColorButtonImage(Button button) {
        ImageView imageView = (ImageView) button.getGraphic();
        Image image = imageView.getImage();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage finalImage = new WritableImage(width, height);

        PixelReader reader = image.getPixelReader();
        PixelWriter writer = finalImage.getPixelWriter();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (reader.getColor(x, y).equals(Color.BLACK)) {
                    writer.setArgb(x, y, 0xff36b9ff);
                }
                else {
                    writer.setArgb(x, y, reader.getArgb(x, y));
                }
            }
        }

        imageView.setImage(finalImage);
    }

    @FXML
    void homeButtonPressed(ActionEvent event) throws MalformedURLException {
        setButtonImages();
        reColorButtonImage(homeButton);

    }

    @FXML
    void profileButtonPressed(ActionEvent event) throws MalformedURLException {
        setButtonImages();
        reColorButtonImage(profileButton);
    }

    @FXML
    void searchButtonPressed(ActionEvent event) throws MalformedURLException {
        setButtonImages();
        reColorButtonImage(searchButton);
    }

    @FXML
    void settingsButtonPressed(ActionEvent event) throws MalformedURLException {
        setButtonImages();
        reColorButtonImage(settingsButton);
    }

}
