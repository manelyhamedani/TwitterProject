package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.callback.ResponseCallback;
import com.manely.ap.project.client.callback.TimelineResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.ButtonUtility;
import com.manely.ap.project.client.util.TweetUtility;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;
import com.manely.ap.project.common.model.Tweet.Kind;
import com.manely.ap.project.common.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.manely.ap.project.client.util.ButtonUtility.setColorButtonImage;


public class HomePage {
    private static HomePage instance;
    private static Scene scene;

    public static HomePage getInstance() {
        return instance;
    }

    public static Scene getScene() {
        return scene;
    }

    private final ObservableList<Tweet> profTweets = FXCollections.observableArrayList();
    private final ObservableList<Tweet> filterTweets = FXCollections.observableArrayList();
    private final ObservableList<Profile> users = FXCollections.observableArrayList();
    private final ObservableList<Profile> follows = FXCollections.observableArrayList();

    private Image homeImage;
    private Image profileImage;
    private Image searchImage;
    private Image coloredHomeImage;
    private Image coloredProfileImage;
    private Image coloredSearchImage;
    private User currentProfile;

    @FXML
    private ImageView locationImageView;

    @FXML
    private ImageView websiteImageView;

    @FXML
    private ImageView calendarImageView;

    @FXML
    private Button homeButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button searchButton;

    @FXML
    private VBox profileVBox;

    @FXML
    private ImageView headerImageView;

    @FXML
    private ImageView avatarImageView;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Label nameLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Text bioText;

    @FXML
    private Label locationLabel;

    @FXML
    private Hyperlink websiteHyperlink;

    @FXML
    private Label dateAddedLabel;

    @FXML
    private Label followingLabel;

    @FXML
    private Label followersLabel;

    @FXML
    private ListView<Tweet> profTweetListView;

    @FXML
    private BorderPane root;

    @FXML
    private VBox searchVBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField filterTextField;

    @FXML
    private ListView<Tweet> filterTweetListView;

    @FXML
    private ListView<Profile> searchUserListView;

    @FXML
    private Button backButton;

    @FXML
    private ListView<Profile> followListView;

    @FXML
    private StackPane stackPane;

    public void initialize() throws IOException {
        setButtonImages();

        coloredHomeImage = setColorButtonImage(homeButton, 0xff36b9ff);
        coloredProfileImage = setColorButtonImage(profileButton, 0xff36b9ff);
        coloredSearchImage = setColorButtonImage(searchButton, 0xff36b9ff);

        profTweetListView.setFocusTraversable(false);
        searchUserListView.setFocusTraversable(false);
        filterTweetListView.setFocusTraversable(false);

        profTweetListView.setCellFactory((listView) -> new TweetCell());
        filterTweetListView.setCellFactory((listView) -> new TweetCell());
        searchUserListView.setCellFactory((listView) -> new ProfileCell());
        followListView.setCellFactory((listView) -> new ProfileCell());

        URL location = new URL("file:src/main/resources/location.png");
        URL website = new URL("file:src/main/resources/link.png");
        URL calendar = new URL("file:src/main/resources/calendar.jpg");

        locationImageView.setImage(new Image(location.toString()));
        websiteImageView.setImage(new Image(website.toString()));
        calendarImageView.setImage(new Image(calendar.toString()));

        backButton.setVisible(false);
        backButton.setDisable(true);

        instance = this;
        scene = root.getScene();
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
        resetButtonImages();
        ((ImageView) homeButton.getGraphic()).setImage(coloredHomeImage);

        TimeLine timeLine = new TimeLine();
        timeLine.setUp();
        stackPane.getChildren().set(0, timeLine);

        HashMap<String, String> query = new HashMap<>();
        Type type = new TypeToken<ArrayList<Post>>(){}.getType();
        HttpCall.get(API.TIMELINE, query, type, new TimelineResponseCallback<>(timeLine));

    }

    @FXML
    public void profileButtonPressed() {
        resetButtonImages();
        ((ImageView) profileButton.getGraphic()).setImage(coloredProfileImage);

        ProfilePage profilePage = new ProfilePage();
        profilePage.setUp(Data.getUser(), true);

        stackPane.getChildren().set(0, profilePage);
    }


    @FXML
    void searchButtonPressed() {
        resetButtonImages();
        ((ImageView) searchButton.getGraphic()).setImage(coloredSearchImage);

        searchTextField.clear();
        filterTextField.clear();

        tweetListView.setVisible(false);
        tweetListView.setDisable(true);
        profileVBox.setDisable(true);
        profileVBox.setVisible(false);
        followListView.setVisible(false);
        followListView.setDisable(true);

        searchVBox.setVisible(true);
        searchVBox.setDisable(false);

    }

    @FXML
    void addTweetButtonPressed(ActionEvent event) {
        AddTweet addTweet = new AddTweet();
        addTweet.setTweetKind(Kind.TWEET);
        Scene scene = new Scene(addTweet);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void button1Pressed() {

    }
// set buttons in profiling
    @FXML
    void button2Pressed() {

    }

    @FXML
    void websiteHyperlinkClicked() {
        String link = websiteHyperlink.getText();
        if (!link.isBlank()) {
            try {
                URL url = new URL(link);
                Desktop.getDesktop().browse(url.toURI());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setFollowsListView() {
        tweetListView.setVisible(false);
        tweetListView.setDisable(true);
        profileVBox.setDisable(true);
        profileVBox.setVisible(false);
        searchVBox.setVisible(false);
        searchVBox.setDisable(true);

        followListView.setVisible(true);
        followListView.setDisable(false);

        follows.clear();
    }

    @FXML
    void followingHyperlinkClicked() {
        setFollowsListView();

        HashMap<String, String> query = new HashMap<>();
        query.put("username", currentProfile.getUsername());
        Type type = new TypeToken<ArrayList<User>>(){}.getType();

        HttpCall.get(API.LIST_FOLLOWING, query, type, new FetchUsersResponseCallback<>(followListView, follows, profileVBox));
    }

    @FXML
    void followersHyperlinkClicked() {
        setFollowsListView();
        HashMap<String, String> query = new HashMap<>();
        query.put("username", currentProfile.getUsername());
        Type type = new TypeToken<ArrayList<User>>(){}.getType();

        HttpCall.get(API.LIST_FOLLOWERS, query, type, new FetchUsersResponseCallback<>(followListView, follows, profileVBox));
    }




    @FXML
    void searchUserButtonPressed() {
        searchUserListView.setDisable(false);
        searchUserListView.setVisible(true);
        filterTweetListView.setVisible(false);
        filterTweetListView.setDisable(true);

        String search = searchTextField.getText();
        if (!search.isBlank()) {

            searchUserListView.setItems(null);
            users.clear();

            HashMap<String, String> query = new HashMap<>();
            query.put("quest", search);
            Type type = new TypeToken<ArrayList<User>>(){}.getType();

            HttpCall.get(API.SEARCH, query, type, new FetchUsersResponseCallback<>(searchUserListView, users, searchVBox));
        }
    }

    private class FetchProfileEventHandler implements EventHandler<ActionEvent> {
        private final User user;
        private final Node preNode;

        public FetchProfileEventHandler(User user, Node node) {
            this.user = user;
            this.preNode = node;
        }

        @Override
        public void handle(ActionEvent event) {
            currentTweets = profTweets;
            currentList = profTweetListView;

            resetButtonImages();
            ((ImageView) profileButton.getGraphic()).setImage(coloredProfileImage);

            TweetUtility.setUp(profTweetListView, profTweets, TweetUtility.Kind.PROFILE, user.getUsername());

            currentProfile = user;
            setProfile(user, true);

            backButton.setDisable(false);
            backButton.setVisible(true);

            backButton.setOnAction((e) -> {
                profileVBox.setDisable(true);
                profileVBox.setVisible(false);

                preNode.setVisible(true);
                preNode.setDisable(false);
            });

            tweetListView.setDisable(true);
            tweetListView.setVisible(false);
            searchVBox.setVisible(false);
            searchVBox.setDisable(true);
            followListView.setDisable(true);
            followListView.setVisible(false);

            profileVBox.setDisable(false);
            profileVBox.setVisible(true);
        }
    }

    @FXML
    void filterButtonPressed() {
        searchUserListView.setDisable(true);
        searchUserListView.setVisible(false);
        filterTweetListView.setVisible(true);
        filterTweetListView.setDisable(false);

        String filter = filterTextField.getText();
        if (!filter.isBlank()) {
            currentList = filterTweetListView;
            currentTweets = filterTweets;

            filterTweetListView.setItems(null);
            filterTweets.clear();

            HashMap<String, String> query = new HashMap<>();
            query.put("quest", "#" + filter);
            Type type = new TypeToken<ArrayList<Post>>(){}.getType();

            HttpCall.get(API.FILTER, query, type,
                new ResponseCallback<>() {
                    @Override
                    public void run() {
                        if (getResponse().isSuccess()) {
                            ArrayList<Post> posts = (ArrayList<Post>) getResponse().getContent();
                            for (Post post : posts) {
                                if (post instanceof Retweet) {
                                    Tweet retweet = new Tweet();
                                    retweet.setRetweet((Retweet) post);
                                    filterTweets.add(retweet);
                                }

                                else if (post instanceof com.manely.ap.project.common.model.Tweet) {
                                    Tweet tweet = new Tweet();
                                    tweet.setTweet((com.manely.ap.project.common.model.Tweet) post);
                                    filterTweets.add(tweet);

                                    tweet.retweetedProperty().addListener((observableValue, oldValue, newValue) -> {
                                        if (newValue) {
                                            Platform.runLater(() -> com.manely.ap.project.client.controller.Scene.gotoHomePage(HomePage.getScene()));
                                        }
                                    });

                                }
                            }
                            Platform.runLater(() -> {
                                filterTweets.sort((o1, o2) -> {
                                    long id1 = o1.getTweet().getDate().getTime();
                                    long id2 = o2.getTweet().getDate().getTime();
                                    return Long.compare(id1, id2) * -1;
                                });
                                TweetUtility.setRefs(filterTweets);
                                filterTweetListView.setItems(filterTweets);
                            });
                        }
                        else {
                            System.out.println(getResponse().getMessage());
                        }
                    }
            });
        }

    }

    @FXML
    void logoutButtonPressed() {
        Data.reset();
        com.manely.ap.project.client.controller.Scene.changeScene("entry.fxml");
    }
}
