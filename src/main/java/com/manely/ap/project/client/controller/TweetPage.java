package com.manely.ap.project.client.controller;


import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.FetchTweetCallback;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TweetPage extends ListView<Tweet> {

    @FXML
    private ListView<Tweet> tweetsListView;

    private ObservableList<Tweet> tweets = FXCollections.observableArrayList();

    public TweetPage() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tweet-page.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        tweetsListView.setFocusTraversable(false);
        tweetsListView.setCellFactory((listView) -> {
            TweetCell cell = new TweetCell();
            if (tweetsListView.getChildrenUnmodifiable().size() == 0) {
                cell.setEffect(new DropShadow(20, Color.BLACK));
            }
            return cell;
        });
        tweetsListView.setItems(tweets);
    }

    public void setUp(Tweet tweet, Node preNode) {

        int tweetId = tweet.getTweet().getId();

        HashMap<String, String> query = new HashMap<>();
        query.put("id", String.valueOf(tweetId));

        Type type = new TypeToken<ArrayList<Post>>(){}.getType();

        HttpCall.get(API.FETCH_TWEET_REPLIES, query, type, new FetchTweetCallback<>(tweets, this, tweet));

        HomePage.getInstance().setScreen(this);
        HomePage.getInstance().setBackButton(preNode);
    }

}
