package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.ResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.Binding;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import static com.manely.ap.project.client.util.ButtonUtility.setColorButtonImage;


public class HomePage {

    private ObservableList<Tweet> tweets = FXCollections.observableArrayList();

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

    @FXML
    private Button addTweetButton;

    @FXML
    private ListView<Tweet> tweetListView;

    public void initialize() throws IOException {
        setButtonImages();

        coloredHomeImage = setColorButtonImage(homeButton, 0xff36b9ff);
        coloredProfileImage = setColorButtonImage(profileButton, 0xff36b9ff);
        coloredSearchImage = setColorButtonImage(searchButton, 0xff36b9ff);
        coloredSettingsImage = setColorButtonImage(settingsButton, 0xff36b9ff);

        tweetListView.setFocusTraversable(false);

        tweetListView.setCellFactory((listView) -> {
            TweetCell cell = new TweetCell();
            cell.setFocusTraversable(false);
            return cell;
        });

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
    void homeButtonPressed() {
        resetButtonImages();
        ((ImageView) homeButton.getGraphic()).setImage(coloredHomeImage);

        tweetListView.setItems(null);
        tweets.clear();

        HashMap<String, String> query = new HashMap<>();
        Type type = new TypeToken<ArrayList<Post>>(){}.getType();
        HttpCall.get(API.TIMELINE, query,
                type,
                new ResponseCallback<>() {
                    @Override
                    public void run() {
                        if (getResponse().isSuccess()) {
                            ArrayList<Post> posts = (ArrayList<Post>) getResponse().getContent();
                            for (Post post : posts) {
                                if (post instanceof Retweet) {
                                    Tweet retweet = new Tweet();
                                    retweet.setRetweet((Retweet) post);
                                    tweets.add(retweet);
                                }
                                else if (post instanceof com.manely.ap.project.common.model.Tweet) {
                                    Tweet tweet = new Tweet();
                                    tweet.setTweet((com.manely.ap.project.common.model.Tweet) post);
                                    tweets.add(tweet);

                                    tweet.retweetedProperty().addListener(new ChangeListener<Boolean>() {
                                        @Override
                                        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                                            if (newValue) {
                                                Platform.runLater(() -> homeButtonPressed());
                                            }
                                        }
                                    });
                                }
                                Data.addTimelinePost(post.getPostID());
                            }
                            tweets.sort((o1, o2) -> {
                                long id1 = o1.getTweet().getDate().getTime();
                                long id2 = o2.getTweet().getDate().getTime();
                                return Long.compare(id1, id2) * -1;
                            });
                            Binding.setBindings(tweets);
                            Platform.runLater(() -> tweetListView.setItems(tweets));
                        }
                        else {
                            System.out.println(getResponse().getMessage());
                        }
                    }
                });
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

    @FXML
    void addTweetButtonPressed() {

    }

}
