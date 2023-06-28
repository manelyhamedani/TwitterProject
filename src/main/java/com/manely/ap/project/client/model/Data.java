package com.manely.ap.project.client.model;

import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.util.ArrayList;


public class Data {
    private static User user;
    private static ArrayList<Post> timeLinePosts = new ArrayList<>();


    public static User getUser() {
        return user;
    }

    public static void setUser(User u) {
        user = u;
    }

    public static ArrayList<Post> getTimeLinePosts() {
        return timeLinePosts;
    }

    public static void setTimeLinePosts(ArrayList<Post> posts) {
        timeLinePosts = posts;
    }

}
