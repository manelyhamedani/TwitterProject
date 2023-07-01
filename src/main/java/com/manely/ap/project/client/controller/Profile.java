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

    public synchronized static void block_unblock(Button blockButton, User user) {
        String path;
        if (blockButton.getText().equals("Block")) {
            path = API.BLOCK;
        }
        else {
            path = API.UNBLOCK;
        }

        boolean block = path.equals(API.BLOCK);

        HashMap<String, String> query = new HashMap<>();
        query.put("username", user.getUsername());

        HttpCall.get(path, query, Object.class,
            new ResponseCallback<>() {
                @Override
                public void run() {
                    if (getResponse().isSuccess()) {
                        if (block) {
                            Data.getUser().getFollowings().remove(user.getUsername());
                            Data.getUser().getFollowers().remove(user.getUsername());
                            Platform.runLater(() -> blockButton.setText("Unblock"));
                        }
                        else {
                            Platform.runLater(() -> blockButton.setText("Block"));
                        }
                    }
                    else {
                        System.out.println(getResponse().getMessage());
                    }
                }
            });

    }

    public synchronized static void follow_unfollow(Button followButton, User user) {
        String path;
        if (followButton.getText().equals("Follow")) {
            path = API.FOLLOW;
        }
        else {
            path = API.UNFOLLOW;
        }

        boolean follow = path.equals(API.FOLLOW);

        HashMap<String, String> query = new HashMap<>();
        query.put("username", user.getUsername());

        HttpCall.get(path, query, Object.class,
            new ResponseCallback<>() {
                @Override
                public void run() {
                    if (getResponse().isSuccess()) {
                        if (follow) {
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
       follow_unfollow(followButton, user);
    }

}
