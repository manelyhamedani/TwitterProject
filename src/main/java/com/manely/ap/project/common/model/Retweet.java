package com.manely.ap.project.common.model;


import java.io.Serializable;

public class Retweet extends Post implements Serializable {
    private Tweet tweet;

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }

}
