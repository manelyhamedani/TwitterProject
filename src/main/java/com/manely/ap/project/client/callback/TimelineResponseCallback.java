package com.manely.ap.project.client.callback;

import com.manely.ap.project.client.controller.TimeLine;
import com.manely.ap.project.client.controller.Tweet;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.TweetUtility;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class TimelineResponseCallback<T> extends ResponseCallback<T> {
    private final ObservableList<Tweet> tweets;
    private final TimeLine timeLine;

    public TimelineResponseCallback(TimeLine timeLine) {
        this.tweets = timeLine.getTweets();
        this.timeLine = timeLine;
    }

    @Override
    public void run() {

        if (getResponse().isSuccess()) {

            ArrayList<Post> posts = (ArrayList<Post>) getResponse().getContent();

            for (Post post : posts) {

                Tweet tweet = new Tweet();

                if (post instanceof Retweet) {
                    tweet.setRetweet((Retweet) post);
                }

                else if (post instanceof com.manely.ap.project.common.model.Tweet) {
                    tweet.setTweet((com.manely.ap.project.common.model.Tweet) post);
                }

                if (tweets.contains(tweet)) {
                    continue;
                }

                Data.addTimelinePost(post.getPostID());
                Platform.runLater(() -> tweets.add(tweet));

            }

            Platform.runLater(() -> {

                tweets.sort((o1, o2) -> {
                    int id1 = o1.getTweet().getPostID();
                    int id2 = o2.getTweet().getPostID();
                    return Integer.compare(id1, id2) * -1;
                });

                TweetUtility.setRefs(tweets);
                timeLine.setUpScroll();
            });
        }
        else {
            System.out.println(getResponse().getMessage());
        }
    }
}
