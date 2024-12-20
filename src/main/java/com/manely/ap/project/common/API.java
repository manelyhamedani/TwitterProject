package com.manely.ap.project.common;

public class API {
    public static final int PORT = 7070;
    public static final String IP_ADDRESS = "localhost";
    public static final String BASE_URL = "http://" + IP_ADDRESS + ":" + PORT;

    public static final String LOGIN = "/login";
    public static final String SIGNUP = "/signup";
    public static final String SET_USER_IMAGE = "/set-user-image";
    public static final String SET_USER_INFO = "/set-user-info";
    public static final String FOLLOW = "/follow";
    public static final String UNFOLLOW = "/unfollow";
    public static final String LIST_FOLLOWERS = "/list-followers";
    public static final String LIST_FOLLOWING = "/list-following";
    public static final String SEARCH = "/search";
    public static final String TWEET = "/tweet";
    public static final String RETWEET = "/retweet";
    public static final String QUOTE = "/quote";
    public static final String LIKE = "/like";
    public static final String UNLIKE = "/unlike";
    public static final String REPLY = "/reply";
    public static final String BLOCK = "/block";
    public static final String UNBLOCK = "/unblock";
    public static final String TIMELINE = "/timeline";
    public static final String FILTER = "/filter";
    public static final String FETCH_USER_POSTS = "/fetch-user-posts";
    public static final String FETCH_TWEET_REPLIES = "/fetch-tweet-replies";
    public static final String VOTE = "/vote";
}
