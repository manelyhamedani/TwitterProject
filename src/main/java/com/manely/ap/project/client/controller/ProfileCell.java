package com.manely.ap.project.client.controller;

import javafx.scene.control.ListCell;

public class ProfileCell extends ListCell<Profile> {

    @Override
    protected void updateItem(Profile item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        }
        else {
            setGraphic(item);
        }

    }
}
