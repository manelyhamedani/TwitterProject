package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.ResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Timeline extends Group {
    private ObservableList<Tweet> tweets = FXCollections.observableArrayList();

    @FXML
    private ListView<Tweet> timeLineListView;

    @FXML
    private Button tweetButton;

    public void initialize() {
        timeLineListView.setCellFactory((listView) -> new TweetCell());
        HashMap<String, String> query = new HashMap<>();
        query.put("date", String.valueOf(new Date().getTime()));
        Type type = new TypeToken<ArrayList<Post>>(){}.getType();
        HttpCall.get(API.TIMELINE, query,
                type,
                new ResponseCallback<>() {
                    @Override
                    public void run() {
                        if (getResponse().isSuccess()) {
                            ArrayList<Post> posts = (ArrayList<Post>) getResponse().getContent();
                            for (Post post : posts) {
                                if (post instanceof com.manely.ap.project.common.model.Tweet) {
                                    Tweet tweet = new Tweet((com.manely.ap.project.common.model.Tweet) post);
                                    try {
                                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tweet.fxml"));
                                        fxmlLoader.setRoot(tweet);
                                        fxmlLoader.load();
                                        tweets.add(tweet);
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            Data.setTimeLinePosts(posts);
                            timeLineListView.setItems(tweets);
                        }
                        else {
                            System.out.println(getResponse().getMessage());
                        }
                    }
                });
    }

    @FXML
    void tweetButtonPressed(ActionEvent event) {

    }

}
