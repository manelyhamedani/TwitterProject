package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.TimelineResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TimeLine extends ListView<Tweet> {

    @FXML
    private ListView<Tweet> timeLineListView;

    private ObservableList<Tweet> tweets = FXCollections.observableArrayList();

    public TimeLine() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("timeline.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

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

    public void setUpScroll() {
        ScrollBar bar = (ScrollBar) timeLineListView.lookup(".scroll-bar:vertical");
        bar.valueProperty().addListener((observableValue, ov, nv) -> {

            HashMap<String, String> query = new HashMap<>();
            Type type = new TypeToken<ArrayList<Post>>() {
            }.getType();

            if (nv.equals(bar.getMax())) {
                int id = Data.getEarliestTimelinePost();
                if (id != 0) {
                    query.put("id", String.valueOf(id));
                }
            } else if (!nv.equals(bar.getMin())) {
                return;
            }

            HttpCall.get(API.TIMELINE, query, type, new TimelineResponseCallback<>(this));
        });
    }

}

