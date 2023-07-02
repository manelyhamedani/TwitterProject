package com.manely.ap.project.client.callback;

import com.manely.ap.project.client.controller.Tweet;
import com.manely.ap.project.client.util.TweetUtility;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class FetchTweetCallback<T> extends ResponseCallback<T> {
    private final ObservableList<Tweet> tweets;
    private final Tweet lastItem;
    private final Node parent;

    public FetchTweetCallback(ObservableList<Tweet> tweets, Node parent, Tweet lastItem) {
        this.tweets = tweets;
        this.parent = parent;
        this.lastItem = lastItem;
    }

    @Override
    public void run() {
        if (getResponse().isSuccess()) {
            ArrayList<Post> posts = (ArrayList<Post>) getResponse().getContent();
            for (Post post : posts) {
                Tweet tweet = new Tweet();
                if (post instanceof Retweet) {
                    tweet.setRetweet((Retweet) post, parent);
                }
                else if (post instanceof com.manely.ap.project.common.model.Tweet) {
                    tweet.setTweet((com.manely.ap.project.common.model.Tweet) post, parent);
                }
                Platform.runLater(() -> tweets.add(tweet));
            }
            Platform.runLater(() -> {
                tweets.sort((o1, o2) -> {
                    long id1 = o1.getTweet().getDate().getTime();
                    long id2 = o2.getTweet().getDate().getTime();
                    return Long.compare(id1, id2) * -1;
                });
                if (lastItem != null) {
                    tweets.add(0, lastItem);
                }
                TweetUtility.setRefs(tweets);
            });
        }
        else {
            System.out.println(getResponse().getMessage());
        }
    }
}
