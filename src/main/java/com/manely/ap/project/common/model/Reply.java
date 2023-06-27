package com.manely.ap.project.common.model;

import java.io.Serializable;

public class Reply extends Tweet implements Serializable {
    private Tweet tweet;

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
