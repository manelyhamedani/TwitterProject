package com.manely.ap.project.client.util;

import com.manely.ap.project.client.controller.Tweet;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

import java.util.EventListener;

public class Binding {

    public static void setBindings(ObservableList<Tweet> tweets) {
        for (Tweet t : tweets) {
            Post post = t.getTweet();

            if (post instanceof Retweet) {
                Tweet ref = getRefTweet(tweets, ((Retweet) post).getTweet().getId());
                if (ref != null) {
                    t.likeProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                            if (newValue) {
                                ref.like();
                            }
                            else {
                                ref.unlike();
                            }
                        }
                    });

                    ref.likeProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                            if (newValue) {
                                t.like();
                            }
                            else {
                                t.unlike();
                            }
                        }
                    });
                }
            }
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
