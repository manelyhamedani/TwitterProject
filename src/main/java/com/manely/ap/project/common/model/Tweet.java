package com.manely.ap.project.common.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Tweet extends Post implements Serializable {
    private String text;
    private Image[] images;
    private int likesCount;
    private int retweetsCount;
    private int commentsCount;
    private int quotesCount;
    private ArrayList<String> likes;

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public int getQuotesCount() {
        return quotesCount;
    }

    public void setQuotesCount(int quotesCount) {
        this.quotesCount = quotesCount;
    }

    private static final int MAX_TEXT_LENGTH = 280;
    private static final int MEDIA_COUNT_LIMIT = 4;

    public int getImageCount() {
        if (images == null)
            return 0;
        return images.length;
    }

    public Error validateTweet() {
        if (text == null && images == null)
            return Error.NULL_TWEET;
        if (!validateText())
            return Error.TEXT_TOO_LONG;
        if (!validateMediaCount())
            return Error.MEDIA_LIMIT_EXCEEDED;
        return null;
    }

    private boolean validateText() {
        if (text != null)
            return (text.length() <= MAX_TEXT_LENGTH);
        return true;
    }

    private boolean validateMediaCount() {
        if (images != null)
            return (images.length <= MEDIA_COUNT_LIMIT);
        return true;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Image[] getImages() {
        return images;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getRetweetsCount() {
        return retweetsCount;
    }

    public void setRetweetsCount(int retweetsCount) {
        this.retweetsCount = retweetsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

}
