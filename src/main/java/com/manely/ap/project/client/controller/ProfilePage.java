package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.UserPostResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfilePage extends ListView<VBox> {

    private static ProfilePage instance;

    public static ProfilePage getInstance() {
        return instance;
    }

    @FXML
    private ListView<VBox> profileListView;

    private ObservableList<VBox> items = FXCollections.observableArrayList();
    private User user;

    public ProfilePage() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("profile-page.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        instance = this;
        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ListView<VBox> getProfileListView() {
        return profileListView;
    }

    public void initialize() {
        profileListView.setFocusTraversable(false);
        profileListView.setItems(items);
    }

    public void setUp(User user, boolean isSelf) {
        this.user = user;
        ProfileInfo profileInfo = new ProfileInfo();
        profileInfo.setUp(this, user, isSelf);

        profileListView.getItems().add(profileInfo);

        HashMap<String, String> query = new HashMap<>();
        query.put("username", user.getUsername());


        Type type = new TypeToken<ArrayList<Post>>(){}.getType();

        HttpCall.get(API.FETCH_USER_POSTS, query, type, new UserPostResponseCallback<>(this));

    }


}
