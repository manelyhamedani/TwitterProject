package com.manely.ap.project.client.controller;

import javafx.scene.control.ListCell;

import java.util.ArrayList;


public class TweetCell extends ListCell<Tweet> {

    @Override
    protected void updateItem(Tweet item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        }
        else {
            setGraphic(item);
        }

    }
}
