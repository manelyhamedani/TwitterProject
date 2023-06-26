package com.manely.ap.project.common.model;

public class Quote extends Tweet {
    private Tweet tweet;

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
