package com.manely.ap.project.client.model;

import com.manely.ap.project.common.model.User;

public class Data {
    private static User user;
    private static int earliestTimelinePost = 0;
    private static int earliestProfPost = 0;

    public static User getUser() {
        return user;
    }

    public static void setUser(User u) {
        user = u;
    }

    public static int getEarliestTimelinePost() {
        return earliestTimelinePost;
    }

    public static int getEarliestProfPost() {
        return earliestProfPost;
    }


    public static void addTimelinePost(int id) {
        if (earliestTimelinePost == 0 || id < earliestTimelinePost) {
            earliestTimelinePost = id;
        }
    }

    public static void addProfPost(int id) {
        if (earliestProfPost == 0 || id < earliestProfPost) {
            earliestProfPost = id;
        }
    }


}
