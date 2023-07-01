package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.ResponseCallback;
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
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

    private final ObservableList<Tweet> tweets = FXCollections.observableArrayList();
    private final ObservableList<Tweet> profTweets = FXCollections.observableArrayList();
    private final ObservableList<Tweet> filterTweets = FXCollections.observableArrayList();
    private final ObservableList<Profile> users = FXCollections.observableArrayList();

    private static ListView<Tweet> currentList;
    private static ObservableList<Tweet> currentTweets;

    private Image homeImage;
    private Image profileImage;
    private Image searchImage;
    private Image coloredHomeImage;
    private Image coloredProfileImage;
    private Image coloredSearchImage;

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
    private ListView<Tweet> tweetListView;

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


    public void initialize() throws IOException {
        setButtonImages();

        coloredHomeImage = setColorButtonImage(homeButton, 0xff36b9ff);
        coloredProfileImage = setColorButtonImage(profileButton, 0xff36b9ff);
        coloredSearchImage = setColorButtonImage(searchButton, 0xff36b9ff);

        tweetListView.setFocusTraversable(false);
        profTweetListView.setFocusTraversable(false);
        searchUserListView.setFocusTraversable(false);
        filterTweetListView.setFocusTraversable(false);

        tweetListView.setCellFactory((listView) -> new TweetCell());

        profTweetListView.setCellFactory((listView) -> new TweetCell());

        filterTweetListView.setCellFactory((listView) -> new TweetCell());

        searchUserListView.setCellFactory((listView) -> new ProfileCell());

        URL location = new URL("file:src/main/resources/location.png");
        URL website = new URL("file:src/main/resources/link.png");
        URL calendar = new URL("file:src/main/resources/calendar.jpg");

        locationImageView.setImage(new Image(location.toString()));
        websiteImageView.setImage(new Image(website.toString()));
        calendarImageView.setImage(new Image(calendar.toString()));

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
        currentList = tweetListView;
        currentTweets = tweets;

        resetButtonImages();
        ((ImageView) homeButton.getGraphic()).setImage(coloredHomeImage);

        TweetUtility.setUp(tweetListView, tweets, TweetUtility.Kind.TIMELINE, null);

        profileVBox.setVisible(false);
        profileVBox.setDisable(true);
        searchVBox.setVisible(false);
        searchVBox.setDisable(true);

        tweetListView.setVisible(true);
        tweetListView.setDisable(false);

        tweetListView.setItems(null);
        tweets.clear();

        HashMap<String, String> query = new HashMap<>();
        Type type = new TypeToken<ArrayList<Post>>(){}.getType();
        HttpCall.get(API.TIMELINE, query, type, new TweetUtility.FetchTweetResponseCallback<>(tweetListView, tweets, TweetUtility.Kind.TIMELINE));


    }


    public static ObservableList<Tweet> getCurrentTweets() {
        return currentTweets;
    }

    public static ListView<Tweet> getCurrentList() {
        return currentList;
    }

    @FXML
    public void profileButtonPressed() {
        currentTweets = profTweets;
        currentList = profTweetListView;

        resetButtonImages();
        ((ImageView) profileButton.getGraphic()).setImage(coloredProfileImage);

        TweetUtility.setUp(profTweetListView, profTweets, TweetUtility.Kind.PROFILE, Data.getUser().getUsername());

        setProfile(Data.getUser(), true);

        tweetListView.setDisable(true);
        tweetListView.setVisible(false);
        searchVBox.setVisible(false);
        searchVBox.setDisable(true);

        profileVBox.setDisable(false);
        profileVBox.setVisible(true);
    }

    public void setProfile(User user, boolean self) {
        Image avatar = null, header = null;

        if (user.getAvatar() == null) {
            try {
                URL url = new URL("file:src/main/resources/avatar.png");
                avatar = new Image(url.toString());
            }
            catch (MalformedURLException ignore) {
            }
        }
        else {
            avatar = new Image(new ByteArrayInputStream(user.getAvatar().getBytes()));
        }

        if (avatar != null) {
            ButtonUtility.setRoundedImageView(avatarImageView, avatar);
        }

        if (user.getHeader() == null) {
            try {
                URL url = new URL("file:src/main/resources/grey.jpg");
                header = new Image(url.toString());
            }
            catch (MalformedURLException ignore) {
            }
        }
        else {
            header = new Image(new ByteArrayInputStream(user.getHeader().getBytes()));
        }

        if (header != null) {
            headerImageView.setImage(header);
        }

        if (self) {
            button2.setVisible(true);
            button2.setDisable(false);
            button2.setText("Edit Profile");
            button1.setDisable(true);
            button1.setVisible(false);
        }
        else {
            button2.setVisible(true);
            button2.setDisable(false);
            button1.setDisable(false);
            button1.setVisible(true);
            button1.setText("Follow");
            button2.setText("Block");
        }

        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        usernameLabel.setText("@" + user.getUsername());
        bioText.setText(user.getInfo().getBio());
        locationLabel.setText(user.getInfo().getLocation());
        websiteHyperlink.setText(user.getInfo().getWebsite());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(user.getDateAdded());
        String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        int year = calendar.get(Calendar.YEAR);
        dateAddedLabel.setText("Joined " + monthName + " " + year);

        followingLabel.setText(String.valueOf(user.getFollowingCount()));
        followersLabel.setText(String.valueOf(user.getFollowersCount()));

        int lastTweetId = Data.getEarliestProfPost();
        HashMap<String, String> query = new HashMap<>();
        query.put("username", user.getUsername());
        if (lastTweetId != 0) {
            query.put("id", String.valueOf(lastTweetId));
        }
        Type type = new TypeToken<ArrayList<Post>>(){}.getType();
        HttpCall.get(API.FETCH_USER_POSTS, query, type, new TweetUtility.FetchTweetResponseCallback<>(profTweetListView, profTweets, TweetUtility.Kind.PROFILE));
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

    @FXML
    void button2Pressed() {

    }

    @FXML
    void websiteHyperlinkClicked() {

    }

    @FXML
    void followingHyperlinkClicked() {

    }

    @FXML
    void followersHyperlinkClicked() {

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

            HttpCall.get(API.SEARCH, query, type,
                new ResponseCallback<>() {
                    @Override
                    public void run() {
                        if (getResponse().isSuccess()) {
                            ArrayList<User> result = (ArrayList<User>) getResponse().getContent();
                            for (User user : result) {
                                Profile profile = new Profile();
                                profile.setProfile(user);
                                users.add(profile);
                                FetchProfileEventHandler handler = new FetchProfileEventHandler(user);
                                profile.getAvatarButton().setOnAction(handler);
                                profile.getNameHyperlink().setOnAction(handler);
                                profile.getUsernameHyperlink().setOnAction(handler);
                            }
                            searchUserListView.setItems(users);
                        }
                        else {
                            System.out.println(getResponse().getMessage());
                        }
                    }
                });
        }
    }

    private class FetchProfileEventHandler implements EventHandler<ActionEvent> {
        private final User user;

        public FetchProfileEventHandler(User user) {
            this.user = user;
        }

        @Override
        public void handle(ActionEvent event) {
            currentTweets = profTweets;
            currentList = profTweetListView;

            resetButtonImages();
            ((ImageView) profileButton.getGraphic()).setImage(coloredProfileImage);

            TweetUtility.setUp(profTweetListView, profTweets, TweetUtility.Kind.PROFILE, user.getUsername());

            setProfile(user, true);

            tweetListView.setDisable(true);
            tweetListView.setVisible(false);
            searchVBox.setVisible(false);
            searchVBox.setDisable(true);

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

}
