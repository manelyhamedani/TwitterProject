package com.manely.ap.project.client.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class Timeline extends Group {
    private final ObservableList<Tweet> tweets = FXCollections.observableArrayList();

    @FXML
    private ListView<Tweet> timeLineListView;

    @FXML
    private Button tweetButton;

    @FXML
    void tweetButtonPressed(ActionEvent event) {

    }

}
