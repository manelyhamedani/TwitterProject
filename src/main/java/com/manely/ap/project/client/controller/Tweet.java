package com.manely.ap.project.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class Tweet extends HBox {

    @FXML
    private Circle avatarCircle;

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

    public void initialize() {

    }

    public Tweet(com.manely.ap.project.common.model.Tweet tweet) {

    }

    @FXML
    void commentButtonPressed(ActionEvent event) {

    }

    @FXML
    void likeButtonPressed(ActionEvent event) {

    }

    @FXML
    void nameLinkOnMouseMoved(MouseEvent event) {

    }

    @FXML
    void quoteButtonPressed(ActionEvent event) {

    }

    @FXML
    void retweetButtonPressed(ActionEvent event) {

    }

    @FXML
    void usernameLinkOnMouseMoved(MouseEvent event) {

    }

}
