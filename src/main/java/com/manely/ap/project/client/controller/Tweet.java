package com.manely.ap.project.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.manely.ap.project.client.util.ButtonUtility.setColorButtonImage;

public class Tweet extends HBox {
    private Image retweetImage;
    private Image coloredRetweetImage;
    private Image likeImage;
    private Image coloredLikeImage;
    private boolean liked = false;
    private boolean retweeted = false;

    @FXML
    private Button avatarButton;

    @FXML
    private Button commentButton;

    @FXML
    private Label commentCountLabel;

    @FXML
    private Button likeButton;

    @FXML
    private Label likeCountLabel;

    @FXML
    private Hyperlink nameLink;

    @FXML
    private Button quoteButton;

    @FXML
    private Label quoteCountLabel;

    @FXML
    private Button retweetButton;

    @FXML
    private Label retweetCountLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private VBox tweetVBox;

    @FXML
    private Hyperlink usernameLink;

    public void initialize() throws IOException {
       setButtonImages();

       coloredRetweetImage = setColorButtonImage(retweetButton);
       coloredLikeImage = setColorButtonImage(likeButton);

    }

    private void setButtonImages() throws MalformedURLException {
        URL comment = new URL("file:/Users/melody/Downloads/comment.png");
        URL retweet = new URL("file:/Users/melody/Downloads/retweet.png");
        URL quote = new URL("file:/Users/melody/Downloads/quote.png");
        URL like = new URL("file:/Users/melody/Downloads/like.png");

        Image commentImage = new Image(comment.toString());
        retweetImage = new Image(retweet.toString());
        Image quoteImage = new Image(quote.toString());
        likeImage = new Image(like.toString());

        ((ImageView) commentButton.getGraphic()).setImage(commentImage);
        ((ImageView) retweetButton.getGraphic()).setImage(retweetImage);
        ((ImageView) quoteButton.getGraphic()).setImage(quoteImage);
        ((ImageView) likeButton.getGraphic()).setImage(likeImage);
    }

    public Tweet(com.manely.ap.project.common.model.Tweet tweet) throws IOException {
        if (tweet.getSenderAvatar() != null) {
            Image avatar = new Image(new ByteArrayInputStream(tweet.getSenderAvatar().getBytes()));
            ((ImageView) avatarButton.getGraphic()).setImage(avatar);
        }

        nameLink.setText(tweet.getSenderName());
        usernameLink.setText(tweet.getSenderUsername());
    }

    @FXML
    void avatarButtonPressed(ActionEvent event) {

    }

    @FXML
    void commentButtonPressed(ActionEvent event) {

    }

    @FXML
    void likeButtonPressed(ActionEvent event) {
        if (liked) {

        }
        else {

        }
    }

    @FXML
    void nameLinkOnMouseMoved(MouseEvent event) {

    }

    @FXML
    void quoteButtonPressed(ActionEvent event) {

    }

    @FXML
    void retweetButtonPressed(ActionEvent event) {
        if (retweeted) {

        }
        else {

        }
    }

    @FXML
    void usernameLinkOnMouseMoved(MouseEvent event) {

    }

}
