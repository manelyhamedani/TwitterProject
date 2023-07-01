package com.manely.ap.project.client.callback;

import com.manely.ap.project.client.controller.HomePage;
import com.manely.ap.project.client.controller.Profile;
import com.manely.ap.project.common.model.User;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ProfileResponseCallback<T> extends ResponseCallback<T> {
    private final ListView<VBox> listView;
    private final ObservableList<VBox> items;

    public ProfileResponseCallback(ListView<VBox> listView, ObservableList<VBox> profiles) {
        this.listView = listView;
        this.items = profiles;
    }

    @Override
    public void run() {
        if (getResponse().isSuccess()) {
            ArrayList<User> result = (ArrayList<User>) getResponse().getContent();
            for (User user : result) {
                Profile profile = new Profile();
                profile.setProfile(user);
                Platform.runLater(() -> profiles.add(profile));
                HomePage.FetchProfileEventHandler handler = new HomePage.FetchProfileEventHandler(user, preNode);
                profile.getAvatarButton().setOnAction(handler);
                profile.getNameHyperlink().setOnAction(handler);
                profile.getUsernameHyperlink().setOnAction(handler);
            }
            Platform.runLater(() -> listView.setItems(profiles));
        }
        else {
            System.out.println(getResponse().getMessage());
        }
    }
}
