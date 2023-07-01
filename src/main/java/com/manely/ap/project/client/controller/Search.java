package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.FilterResponseCallback;
import com.manely.ap.project.client.callback.SearchUserResponseCallback;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Search extends VBox {

    @FXML
    private TextField filterTextField;

    @FXML
    private TextField searchTextField;

    @FXML
    private StackPane stackPane;

    public Search() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("search.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setScreen(Node node) {
        ObservableList<Node> nodes = stackPane.getChildren();
        if (nodes.size() == 0) {
            nodes.add(node);
        }
        else {
            nodes.set(0, node);
        }
    }

    @FXML
    void filterButtonPressed(ActionEvent event) {
        String filter = filterTextField.getText();

        if (!filter.isBlank()) {
            ListView<Tweet> tweetListView = new ListView<>();
            tweetListView.setPrefHeight(USE_COMPUTED_SIZE);
            tweetListView.setPrefWidth(830);
            tweetListView.setMinHeight(USE_PREF_SIZE);
            tweetListView.setMinWidth(USE_PREF_SIZE);
            tweetListView.setMaxHeight(USE_PREF_SIZE);
            tweetListView.setMaxWidth(USE_PREF_SIZE);
            tweetListView.setFocusTraversable(false);

            ObservableList<Tweet> tweets = FXCollections.observableArrayList();

            tweetListView.setCellFactory((listView) -> new TweetCell());
            tweetListView.setItems(tweets);

            setScreen(tweetListView);

            HashMap<String, String> query = new HashMap<>();
            query.put("quest", "#" + filter);
            Type type = new TypeToken<ArrayList<Post>>(){}.getType();

            HttpCall.get(API.FILTER, query, type, new FilterResponseCallback<>(tweets));
        }

    }

    @FXML
    void searchUserButtonPressed(ActionEvent event) {
        String search = searchTextField.getText();

        if (!search.isBlank()) {
            ListView<Profile> profileListView = new ListView<>();
            profileListView.setPrefHeight(USE_COMPUTED_SIZE);
            profileListView.setPrefWidth(830);
            profileListView.setMinHeight(USE_PREF_SIZE);
            profileListView.setMinWidth(USE_PREF_SIZE);
            profileListView.setMaxHeight(USE_PREF_SIZE);
            profileListView.setMaxWidth(USE_PREF_SIZE);
            profileListView.setFocusTraversable(false);

            ObservableList<Profile> profiles = FXCollections.observableArrayList();

            profileListView.setCellFactory((listView) -> new ProfileCell());
            profileListView.setItems(profiles);

            setScreen(profileListView);

            HashMap<String, String> query = new HashMap<>();
            query.put("quest", search);
            Type type = new TypeToken<ArrayList<User>>(){}.getType();

            HttpCall.get(API.SEARCH, query, type, new SearchUserResponseCallback<>(profiles, this));
        }
    }

}
