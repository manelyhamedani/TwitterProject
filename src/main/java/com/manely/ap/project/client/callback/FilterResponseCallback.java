package com.manely.ap.project.client.callback;

import com.manely.ap.project.client.controller.HomePage;
import com.manely.ap.project.client.controller.Tweet;
import com.manely.ap.project.client.util.TweetUtility;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class FilterResponseCallback<T> extends ResponseCallback<T> {
    private ObservableList<Tweet> tweets;

    public FilterResponseCallback(ObservableList<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public void run() {
        if (getResponse().isSuccess()) {
            ArrayList<Post> posts = (ArrayList<Post>) getResponse().getContent();
            for (Post post : posts) {
                if (post instanceof Retweet) {
                    Tweet retweet = new Tweet();
                    retweet.setRetweet((Retweet) post);
                    tweets.add(retweet);
                }

                else if (post instanceof com.manely.ap.project.common.model.Tweet) {
                    Tweet tweet = new Tweet();
                    tweet.setTweet((com.manely.ap.project.common.model.Tweet) post);
                    tweets.add(tweet);
                }
            }
            Platform.runLater(() -> {
                tweets.sort((o1, o2) -> {
                    long id1 = o1.getTweet().getDate().getTime();
                    long id2 = o2.getTweet().getDate().getTime();
                    return Long.compare(id1, id2) * -1;
                });
                TweetUtility.setRefs(tweets);
            });
        }
        else {
            System.out.println(getResponse().getMessage());
        }
    }
}
