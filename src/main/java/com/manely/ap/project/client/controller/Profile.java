package com.manely.ap.project.client.controller;

import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.ResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.ButtonUtility;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Profile extends HBox {
    private User user;

    public User getUser() {
        return user;
    }

    public Profile() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("profile.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Button getAvatarButton() {
        return avatarButton;
    }

    public Hyperlink getNameHyperlink() {
        return nameHyperlink;
    }

    public Hyperlink getUsernameHyperlink() {
        return usernameHyperlink;
    }

    public void setProfile(User user) {
        this.user = user;

        Image avatar = null;

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

        bioLabel.setText(user.getInfo().getBio());
        nameHyperlink.setText(user.getFirstName() + " " + user.getLastName());
        usernameHyperlink.setText(user.getUsername());

        if (user.getUsername().equals(Data.getUser().getUsername())) {
            followButton.setDisable(true);
            followButton.setVisible(false);
        }
        else {
            if (Data.getUser().getFollowings().contains(user.getUsername())) {
                followButton.setText("Following");
            }
            else {
                followButton.setText("Follow");
            }
        }
    }

    @FXML
    private Button avatarButton;

    @FXML
    private ImageView avatarImageView;

    @FXML
    private Label bioLabel;

    @FXML
    private Hyperlink nameHyperlink;

    @FXML
    private Hyperlink usernameHyperlink;

    @FXML
    private Button followButton;

    @FXML
    void followButtonPressed() {
        String path;
        boolean follow = false;
        if (followButton.getText().equals("Follow")) {
            path = API.FOLLOW;
            follow = true;
        }
        else {
            path = API.UNFOLLOW;
        }

        HashMap<String, String> query = new HashMap<>();
        query.put("username", user.getUsername());
        boolean finalFollow = follow;
        HttpCall.get(path, query, Object.class,
            new ResponseCallback<>() {
                @Override
                public void run() {
                    if (getResponse().isSuccess()) {
                        if (finalFollow) {
                            Data.getUser().getFollowings().add(user.getUsername());
                            Platform.runLater(() -> followButton.setText("Following"));
                        }
                        else {
                            Data.getUser().getFollowings().remove(user.getUsername());
                            Platform.runLater(() -> followButton.setText("Follow"));
                        }
                    }
                    else {
                        System.out.println(getResponse().getMessage());
                    }
                }
            });

    }

}
