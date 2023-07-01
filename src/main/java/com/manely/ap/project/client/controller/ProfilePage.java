package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.TimelineResponseCallback;
import com.manely.ap.project.client.callback.UserPostResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.TweetUtility;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfilePage extends ListView<VBox> {

    @FXML
    private ListView<VBox> profileListView;

    private ObservableList<VBox> items = FXCollections.observableArrayList();

    public ProfilePage() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("profile-page.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

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
        ProfileInfo profileInfo = new ProfileInfo();
        profileInfo.setUp(this, user, isSelf);

        profileListView.getItems().add(profileInfo);

        int lastTweetId = Data.getEarliestProfPost();
        HashMap<String, String> query1 = new HashMap<>();
        query1.put("username", user.getUsername());
        if (lastTweetId != 0) {
            query1.put("id", String.valueOf(lastTweetId));
        }

        Type type1 = new TypeToken<ArrayList<Post>>(){}.getType();

        HttpCall.get(API.FETCH_USER_POSTS, query1, type1, new UserPostResponseCallback<>(this));


        ScrollBar bar = (ScrollBar) profileListView.lookup(".scroll-bar:vertical");
        bar.valueProperty().addListener((observableValue, ov, nv) -> {

            HashMap<String, String> query2 = new HashMap<>();
            query2.put("username", user.getUsername());
            Type type2 = new TypeToken<ArrayList<Post>>(){}.getType();

            if (nv.equals(bar.getMax())) {
                int id = Data.getEarliestProfPost();
                if (id != 0) {
                    query2.put("id", String.valueOf(id));
                }
            } else if (!nv.equals(bar.getMin())) {
                return;
            }

            HttpCall.get(API.FETCH_USER_POSTS, query2, type2, new UserPostResponseCallback<>(this));
        });
    }


}
