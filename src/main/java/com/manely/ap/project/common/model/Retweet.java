package com.manely.ap.project.common.model;


public class Retweet extends Post {
    private Tweet tweet;

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

}
