package com.manely.ap.project.client.model;

import com.manely.ap.project.common.model.User;

public class Data {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User u) {
        user = u;
    }

}
