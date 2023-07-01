package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.ButtonUtility;
import com.manely.ap.project.client.util.TweetUtility;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

    public void setUp(User user, boolean isSelf) {
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

    @FXML
    void button1Pressed(ActionEvent event) {

    }

    @FXML
    void button2Pressed(ActionEvent event) {

    }

    @FXML
    void followersHyperlinkClicked(ActionEvent event) {

    }

    @FXML
    void followingHyperlinkClicked(ActionEvent event) {

    }

    @FXML
    void websiteHyperlinkClicked(ActionEvent event) {

    }

}
