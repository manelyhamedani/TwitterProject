package com.manely.ap.project.client.controller;

import com.manely.ap.project.client.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;

import java.io.IOException;

public class TimeLine extends ListView<Tweet> {
    private static TimeLine instance;

    public static TimeLine getInstance() {
        return instance;
    }

    @FXML
    private ListView<Tweet> timeLineListView;

    private ObservableList<Tweet> tweets = FXCollections.observableArrayList();

    public TimeLine() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("timeline.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        instance = this;

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Tweet> getTweets() {
        return tweets;
    }


    public void initialize() {
        timeLineListView.setCellFactory((listView) -> new TweetCell());
        timeLineListView.setFocusTraversable(false);
        timeLineListView.setItems(tweets);

    }

}

