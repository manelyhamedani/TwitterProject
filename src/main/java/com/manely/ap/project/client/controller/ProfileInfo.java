package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.SearchUserResponseCallback;
import com.manely.ap.project.client.util.ButtonUtility;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ProfileInfo extends VBox {

    private User user;
    private ProfilePage page;

    @FXML
    private ImageView avatarImageView;

    @FXML
    private Text bioText;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private ImageView calendarImageView;

    @FXML
    private Label dateAddedLabel;

    @FXML
    private Label followersLabel;

    @FXML
    private Label followingLabel;

    @FXML
    private ImageView headerImageView;

    @FXML
    private ImageView locationImageView;

    @FXML
    private Label locationLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Hyperlink websiteHyperlink;

    @FXML
    private ImageView websiteImageView;

    public ProfileInfo() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("profile-info.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUp(ProfilePage page, User user, boolean isSelf) {
        this.user = user;
        this.page = page;

        try {
            URL location = new URL("file:src/main/resources/location.png");
            URL website = new URL("file:src/main/resources/link.png");
            URL calendar = new URL("file:src/main/resources/calendar.jpg");

            locationImageView.setImage(new Image(location.toString()));
            websiteImageView.setImage(new Image(website.toString()));
            calendarImageView.setImage(new Image(calendar.toString()));
        }
        catch (MalformedURLException ignore) {

        }

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

        if (isSelf) {
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
    }

    private ListView<Profile> makeList() {
        ListView<Profile> list = new ListView<>();
        ObservableList<Profile> items = FXCollections.observableArrayList();

        list.setCellFactory((listView) -> new ProfileCell());
        list.setItems(items);

        list.setPrefHeight(USE_COMPUTED_SIZE);
        list.setPrefWidth(830);
        list.setMinHeight(USE_PREF_SIZE);
        list.setMinWidth(USE_PREF_SIZE);
        list.setMaxHeight(USE_PREF_SIZE);
        list.setMaxWidth(USE_PREF_SIZE);
        list.setFocusTraversable(false);

        return list;
    }

    @FXML
    void button1Pressed(ActionEvent event) {
        Profile.follow_unfollow(button1, user);
    }

    @FXML
    void button2Pressed(ActionEvent event) {
        String cmd = button2.getText();

        if (cmd.equals("Edit Profile")) {

        }
        else {
            Profile.block_unblock(button2, user);
        }
    }

    @FXML
    void followersHyperlinkClicked(ActionEvent event) {
        ListView<Profile> followersList = makeList();

        HashMap<String, String> query = new HashMap<>();
        query.put("username", user.getUsername());
        Type type = new TypeToken<ArrayList<User>>(){}.getType();

        HttpCall.get(API.LIST_FOLLOWERS, query, type, new SearchUserResponseCallback<>(followersList.getItems(), page));
    }

    @FXML
    void followingHyperlinkClicked(ActionEvent event) {
        ListView<Profile> followingList = makeList();

        HashMap<String, String> query = new HashMap<>();
        query.put("username", user.getUsername());
        Type type = new TypeToken<ArrayList<User>>(){}.getType();

        HttpCall.get(API.LIST_FOLLOWING, query, type, new SearchUserResponseCallback<>(followingList.getItems(), page));
    }

    @FXML
    void websiteHyperlinkClicked(ActionEvent event) {
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

}
