package com.manely.ap.project.client.util;

import com.manely.ap.project.client.controller.Tweet;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;

import javafx.collections.ObservableList;

public class TweetUtility {

    public static void setRefs(ObservableList<Tweet> tweets) {

        for (Tweet t : tweets) {
            Post post = t.getTweet();

            if (post instanceof Retweet) {
                Tweet ref = getRefTweet(tweets, ((Retweet) post).getTweet().getId());
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

    private static Tweet getRefTweet(ObservableList<Tweet> tweets, int tweetId) {
        for (Tweet t : tweets) {
            Post post = t.getTweet();

            if (!(post instanceof Retweet) && post.getId() == tweetId) {
                return t;
            }
        }
        return null;
    }
}
