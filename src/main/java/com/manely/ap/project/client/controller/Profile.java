package com.manely.ap.project.client.controller;

import com.manely.ap.project.client.Main;
import com.manely.ap.project.common.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
            avatarImageView.setImage(avatar);
        }

        bioLabel.setText(user.getInfo().getBio());
        nameHyperlink.setText(user.getFirstName() + " " + user.getLastName());
        usernameHyperlink.setText(user.getUsername());
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

    }

}
