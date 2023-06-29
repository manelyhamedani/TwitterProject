package com.manely.ap.project.client.util;

import com.manely.ap.project.client.controller.Tweet;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;

import javafx.collections.ObservableList;

public class TweetUtility {
    private static ObservableList<Tweet> tweetsList;

    public static void setUp(ObservableList<Tweet> tweets) {
        tweetsList = tweets;

        for (Tweet t : tweetsList) {
            Post post = t.getTweet();

            if (post instanceof Retweet) {
                Tweet ref = getRefTweet(((Retweet) post).getTweet().getId());
                setBindings(t, ref);
            }

        }
    }

    private static void setBindings(Tweet t, Tweet ref) {
        if (ref != null) {
            t.likeProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    ref.like();
                }
                else {
                    ref.unlike();
                }
            });

            ref.likeProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    t.like();
                }
                else {
                    t.unlike();
                }
            });

            t.retweetedProperty().addListener((ObservableValue, oldValue, newValue) -> {
                if (newValue) {
                    ref.retweet();
                }
            });

            ref.retweetedProperty().addListener((ObservableValue, oldValue, newValue) -> {
                if (newValue) {
                    t.retweet();
                }
            });
        }
    }

    private static Tweet getRefTweet(int tweetId) {
        for (Tweet t : tweetsList) {
            Post post = t.getTweet();

            if (!(post instanceof Retweet) && post.getId() == tweetId) {
                return t;
            }
        }
        return null;
    }
}
