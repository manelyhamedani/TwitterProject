package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.ButtonUtility;
import com.manely.ap.project.client.util.TweetUtility;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Tweet.Kind;
import com.manely.ap.project.common.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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

    private boolean start;
    private static ListView<Tweet> currentList;
    private static ObservableList<Tweet> currentTweets;

    private Image homeImage;
    private Image profileImage;
    private Image searchImage;
    private Image settingsImage;
    private Image coloredHomeImage;
    private Image coloredProfileImage;
    private Image coloredSearchImage;
    private Image coloredSettingsImage;

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
    private Button settingsButton;

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


    public void initialize() throws IOException {

        start = true;

        setButtonImages();

        coloredHomeImage = setColorButtonImage(homeButton, 0xff36b9ff);
        coloredProfileImage = setColorButtonImage(profileButton, 0xff36b9ff);
        coloredSearchImage = setColorButtonImage(searchButton, 0xff36b9ff);
        coloredSettingsImage = setColorButtonImage(settingsButton, 0xff36b9ff);

        tweetListView.setFocusTraversable(false);
        profTweetListView.setFocusTraversable(false);

        tweetListView.setCellFactory((listView) -> {
            TweetCell cell = new TweetCell();
            cell.setFocusTraversable(false);
            if (cell.getGraphic() != null) {
                cell.setStyle("-fx-background-color: white;" + "-fx-border-color: #989797;");
            }
            return cell;
        });

        profTweetListView.setCellFactory((listView) -> {
            TweetCell cell = new TweetCell();
            cell.setFocusTraversable(false);
            if (cell.getGraphic() != null) {
                cell.setStyle("-fx-background-color: white;" + "-fx-border-color: #989797;");
            }
            return cell;
        });

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
        URL settings = new URL("file:src/main/resources/settings-button.png");

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
    public void homeButtonPressed() {
        currentList = tweetListView;
        currentTweets = tweets;

        resetButtonImages();
        ((ImageView) homeButton.getGraphic()).setImage(coloredHomeImage);

        TweetUtility.setUp(tweetListView, tweets, TweetUtility.Kind.TIMELINE, null);

        profileVBox.setVisible(false);
        profileVBox.setDisable(true);

        tweetListView.setVisible(true);
        tweetListView.setDisable(false);

        if (start) {
            tweetListView.setItems(null);
            tweets.clear();

            HashMap<String, String> query = new HashMap<>();
            Type type = new TypeToken<ArrayList<Post>>(){}.getType();
            HttpCall.get(API.TIMELINE, query, type, new TweetUtility.FetchTweetResponseCallback<>(tweetListView, tweets));

            start = false;
        }

    }


    public static ObservableList<Tweet> getCurrentTweets() {
        return currentTweets;
    }

    public static ListView<Tweet> getCurrentList() {
        return currentList;
    }

    @FXML
    void profileButtonPressed() {
        currentTweets = profTweets;
        currentList = profTweetListView;

        resetButtonImages();
        ((ImageView) profileButton.getGraphic()).setImage(coloredProfileImage);

        TweetUtility.setUp(profTweetListView, profTweets, TweetUtility.Kind.PROFILE, Data.getUser().getUsername());

        setProfile(Data.getUser(), true);


        tweetListView.setDisable(true);
        tweetListView.setVisible(false);

        profileVBox.setDisable(false);
        profileVBox.setVisible(true);

    }

    private void setProfile(User user, boolean self) {
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
        HttpCall.get(API.FETCH_USER_POSTS, query, type, new TweetUtility.FetchTweetResponseCallback<>(profTweetListView, profTweets));
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

}
