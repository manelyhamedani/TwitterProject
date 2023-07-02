package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.callback.TimelineResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Tweet.Kind;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.manely.ap.project.client.util.ButtonUtility.setColorButtonImage;


public class HomePage {
    private static HomePage instance;

    public static HomePage getInstance() {
        return instance;
    }

    public Parent getRoot() {
        return root;
    }

    private Image homeImage;
    private Image profileImage;
    private Image searchImage;
    private Image coloredHomeImage;
    private Image coloredProfileImage;
    private Image coloredSearchImage;
    private ArrayList<Node> preNodes = new ArrayList<>();

    @FXML
    private Button homeButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button searchButton;

    @FXML
    private BorderPane root;

    @FXML
    private StackPane stackPane;

    public void setScreen(Node node) {
        ObservableList<Node> nodes = stackPane.getChildren();
        if (nodes.size() == 0) {
            nodes.add(node);
        }
        else {
            stackPane.getChildren().set(0, node);
        }
    }

    public void setBackButton(Node preNode) {
        preNodes.add(preNode);
    }

    public void initialize() throws IOException {
        setButtonImages();

        coloredHomeImage = setColorButtonImage(homeButton, 0xff36b9ff);
        coloredProfileImage = setColorButtonImage(profileButton, 0xff36b9ff);
        coloredSearchImage = setColorButtonImage(searchButton, 0xff36b9ff);

        instance = this;
    }


    private void setButtonImages() throws MalformedURLException {
        URL home = new URL("file:src/main/resources/home-button.png");
        URL search = new URL("file:src/main/resources/search-button.jpg");
        URL profile = new URL("file:src/main/resources/profile-button.png");

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

    }

    private void resetButtonImages() {
        ((ImageView) homeButton.getGraphic()).setImage(homeImage);
        ((ImageView) profileButton.getGraphic()).setImage(profileImage);
        ((ImageView) searchButton.getGraphic()).setImage(searchImage);
    }

    @FXML
    public void homeButtonPressed() {
        preNodes.clear();
        resetButtonImages();
        ((ImageView) homeButton.getGraphic()).setImage(coloredHomeImage);

        TimeLine timeLine = new TimeLine();
        setScreen(timeLine);

        HashMap<String, String> query = new HashMap<>();
        Type type = new TypeToken<ArrayList<Post>>(){}.getType();
        HttpCall.get(API.TIMELINE, query, type, new TimelineResponseCallback<>(timeLine));
    }

    @FXML
    public void profileButtonPressed() {
        preNodes.clear();
        resetButtonImages();
        ((ImageView) profileButton.getGraphic()).setImage(coloredProfileImage);

        ProfilePage profilePage = new ProfilePage();
        setScreen(profilePage);

        profilePage.setUp(Data.getUser(), true);

    }


    @FXML
    void searchButtonPressed() {
        preNodes.clear();
        resetButtonImages();
        ((ImageView) searchButton.getGraphic()).setImage(coloredSearchImage);

        Search search = new Search();

        setScreen(search);
    }

    @FXML
    void addTweetButtonPressed(ActionEvent event) {
        AddTweet addTweet = new AddTweet();
        addTweet.setTweetKind(Kind.TWEET);
        Scene scene = new Scene(addTweet);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void logoutButtonPressed() {
        Data.reset();
        com.manely.ap.project.client.controller.Scene.changeScene("entry.fxml");
    }

    @FXML
    void backButtonPressed() {
        int lastIndex = preNodes.size() - 1;
        if (lastIndex >= 0) {
            stackPane.getChildren().set(0, preNodes.get(lastIndex));
            preNodes.remove(lastIndex);
        }
    }
}
