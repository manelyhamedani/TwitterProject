package com.manely.ap.project.client.callback;

import com.manely.ap.project.client.controller.HomePage;
import com.manely.ap.project.client.controller.Profile;
import com.manely.ap.project.client.controller.ProfilePage;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.common.model.User;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.util.ArrayList;

public class SearchUserResponseCallback<T> extends ResponseCallback<T> {
    private final ObservableList<Profile> profiles;
    private final Node preNode;

    public SearchUserResponseCallback(ObservableList<Profile> profiles, Node preNode) {
        this.profiles = profiles;
        this.preNode = preNode;
    }

    @Override
    public void run() {
        if (getResponse().isSuccess()) {
            ArrayList<User> result = (ArrayList<User>) getResponse().getContent();
            for (User user : result) {
                Profile profile = new Profile();
                profile.setProfile(user);
                Platform.runLater(() -> profiles.add(profile));
                FetchProfileEventHandler handler = new FetchProfileEventHandler(user, preNode);
                profile.getAvatarButton().setOnAction(handler);
                profile.getNameHyperlink().setOnAction(handler);
                profile.getUsernameHyperlink().setOnAction(handler);
            }
        }
        else {
            System.out.println(getResponse().getMessage());
        }
    }


    private static class FetchProfileEventHandler implements EventHandler<ActionEvent> {
        private final User user;
        private final Node preNode;

        public FetchProfileEventHandler(User user, Node node) {
            this.user = user;
            this.preNode = node;
        }

        @Override
        public void handle(ActionEvent event) {
            ProfilePage profilePage = new ProfilePage();

            boolean isSelf = user.getUsername().equals(Data.getUser().getUsername());

            profilePage.setUp(user, isSelf);

            HomePage.getInstance().setBackButton(preNode);
        }
    }
}




