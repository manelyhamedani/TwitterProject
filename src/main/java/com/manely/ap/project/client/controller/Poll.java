package com.manely.ap.project.client.controller;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.ResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.common.API;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class Poll extends VBox {
    private com.manely.ap.project.common.model.Poll poll;
    private final NumberFormat formatter = NumberFormat.getPercentInstance(Locale.US);

    @FXML
    private Label choice1Label;

    @FXML
    private RadioButton choice1RadioButton;

    @FXML
    private Label choice2Label;

    @FXML
    private RadioButton choice2RadioButton;

    @FXML
    private ToggleGroup vote;

    @FXML
    private VBox root;

    private RadioButton choice3RadioButton = new RadioButton();
    private RadioButton choice4RadioButton = new RadioButton();
    private Label choice3Label = new Label();
    private Label choice4Label = new Label();

    public Poll() {
        formatter.setMaximumFractionDigits(2);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("poll.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HBox makeHBox() {
        HBox hBox = new HBox();
        hBox.setMinHeight(USE_PREF_SIZE);
        hBox.setMinWidth(USE_PREF_SIZE);
        hBox.setMaxHeight(USE_PREF_SIZE);
        hBox.setMaxWidth(USE_PREF_SIZE);
        hBox.setSpacing(8);

        return hBox;
    }

    public void setPoll(com.manely.ap.project.common.model.Poll poll) {
        this.poll = poll;
        choice1RadioButton.setUserData(1);
        choice2RadioButton.setUserData(2);
        choice1RadioButton.setText(poll.getChoice1());
        choice2RadioButton.setText(poll.getChoice2());

        if (poll.getChoice3() != null) {
            choice3RadioButton.setToggleGroup(vote);
            choice3RadioButton.setUserData(3);
            choice3RadioButton.setOnAction((event) -> voteButtonPressed());
            choice3RadioButton.setStyle("-fx-font-family: 'Apple Braille'");
            choice3Label.setStyle("-fx-font-family: 'Apple Braille'");
            choice3RadioButton.setText(poll.getChoice3());
            HBox hBox3 = makeHBox();
            hBox3.getChildren().addAll(choice3RadioButton, choice3Label);
            root.getChildren().add(hBox3);
        }

        if (poll.getChoice4() != null) {
            choice4RadioButton.setToggleGroup(vote);
            choice4RadioButton.setUserData(4);
            choice4RadioButton.setOnAction((event) -> voteButtonPressed());
            choice4RadioButton.setStyle("-fx-font-family: 'Apple Braille'");
            choice4Label.setStyle("-fx-font-family: 'Apple Braille'");
            choice4RadioButton.setText(poll.getChoice4());
            HBox hBox4 = makeHBox();
            hBox4.getChildren().addAll(choice4RadioButton, choice4Label);
            root.getChildren().add(hBox4);
        }

        if (Data.getUser().getVotes().containsKey(poll.getId())) {
            choice1RadioButton.setDisable(true);
            choice2RadioButton.setDisable(true);
            choice3RadioButton.setDisable(true);
            choice4RadioButton.setDisable(true);

            showPoll(poll);
        }
    }

    private void showPoll(com.manely.ap.project.common.model.Poll poll) {
        int choice1Count = poll.getChoice1Count();
        int choice2Count = poll.getChoice2Count();
        int choice3Count = poll.getChoice3Count();
        int choice4Count = poll.getChoice4Count();
        int total = choice1Count + choice2Count + choice3Count + choice4Count;
        double percent1, percent2, percent3, percent4;
        if (total != 0) {
            percent1 = ((double) choice1Count) / total;
            percent2 = ((double) choice2Count) / total;
            percent3 = ((double) choice3Count) / total;
            percent4 = ((double) choice4Count) / total;
        }
        else {
            percent1 = percent2 = percent3 = percent4 = 0;
        }
        Platform.runLater(() -> {
            choice1Label.setText(formatter.format(percent1));
            choice2Label.setText(formatter.format(percent2));
            choice3Label.setText(formatter.format(percent3));
            choice4Label.setText(formatter.format(percent4));
        });
    }

    @FXML
    void voteButtonPressed() {
        choice1RadioButton.setDisable(true);
        choice2RadioButton.setDisable(true);
        choice3RadioButton.setDisable(true);
        choice4RadioButton.setDisable(true);

        int choice = (int) vote.getSelectedToggle().getUserData();
        HashMap<String, String> query = new HashMap<>();
        query.put("id", String.valueOf(poll.getId()));
        query.put("choice", String.valueOf(choice));

        Type type = new TypeToken<com.manely.ap.project.common.model.Poll>(){}.getType();

        HttpCall.get(API.VOTE, query, type,
                new ResponseCallback<>() {
                    @Override
                    public void run() {
                        if (getResponse().isSuccess()) {
                            com.manely.ap.project.common.model.Poll poll = (com.manely.ap.project.common.model.Poll) getResponse().getContent();
                            showPoll(poll);
                        }
                        else {
                            System.out.println(getResponse().getMessage());
                        }
                    }
                });
    }

}
