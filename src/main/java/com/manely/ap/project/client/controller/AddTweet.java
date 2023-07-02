package com.manely.ap.project.client.controller;

import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.callback.ResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.ButtonUtility;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Poll;
import com.manely.ap.project.common.model.Tweet;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AddTweet extends VBox {
    private final Tweet tweet = new Tweet();
    private boolean isPoll = false;
    private final ArrayList<com.manely.ap.project.common.model.Image> media = new ArrayList<>();
    private final SimpleIntegerProperty mediaCount = new SimpleIntegerProperty(0);
    private final FileChooser fileChooser = new FileChooser();

    public AddTweet() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add-tweet.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTweetKind(Tweet.Kind kind) {
        tweet.setKind(kind);
        if (!kind.equals(Tweet.Kind.TWEET)) {
            pollButton.setDisable(true);
        }
    }

    public void setRefTweet(Tweet refTweet) {
        tweet.setRefTweet(refTweet);
    }

    public void initialize() {
        tweetButton.setDisable(true);

        if (Data.getUser().getAvatar() != null) {
            Image avatar = new Image(new ByteArrayInputStream(Data.getUser().getAvatar().getBytes()));
            ButtonUtility.setRoundedImageView(avatarImageView, avatar);
        }
        else {
            try {
                URL avatar = new URL("file:src/main/resources/avatar.png");
                avatarImageView.setImage(new Image(avatar.toString()));
            }
            catch (MalformedURLException ignore) {

            }
        }

        Platform.runLater(() -> tweetTextArea.requestFocus());

        tweetTextArea.textProperty().addListener((observableValue, oldValue, newValue) -> {
            int length = newValue.length();
            if (newValue.length() > 280) {
                tweetTextArea.deleteText(280, length);
                length = 280;
            }
            if (isPoll) {
                if (!newValue.isBlank() && !choice1.getText().isBlank() && !choice2.getText().isBlank()) {
                    tweetButton.setDisable(false);
                }
                else {
                    tweetButton.setDisable(true);
                }
            }
            else {
                if (!newValue.isBlank()) {
                    tweetButton.setDisable(false);
                }
                else if (mediaCount.get() == 0) {
                    tweetButton.setDisable(true);
                }
            }
            textLengthLabel.setText(String.valueOf(length));
        });

        mediaCount.addListener(((observableValue, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                tweetButton.setDisable(false);
            }
            else if (tweetTextArea.getText().isBlank()) {
                tweetButton.setDisable(true);
            }
        }));

    }

    @FXML
    private Label textLengthLabel;

    @FXML
    private Button tweetButton;

    @FXML
    private ImageView avatarImageView;

    @FXML
    private Button mediaButton;

    @FXML
    private Button pollButton;

    @FXML
    private GridPane tweetGridPane;

    @FXML
    private TextArea tweetTextArea;

    @FXML
    private VBox tweetVBox;

    @FXML
    private Label imageErrorLabel;

    @FXML
    void mediaButtonPressed(ActionEvent event) {
        pollButton.setDisable(true);
        imageErrorLabel.setVisible(false);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String[] pathParts = file.getPath().split("\\.");
            String format = pathParts[pathParts.length - 1];
            if (!format.equals("jpg") && !format.equals("png")) {
                imageErrorLabel.setVisible(true);
                return;
            }
            try {
                FileInputStream fis = new FileInputStream(file);
                com.manely.ap.project.common.model.Image img = new com.manely.ap.project.common.model.Image(format, fis.readAllBytes(), com.manely.ap.project.common.model.Image.KIND.TWEET);
                media.add(img);
                mediaCount.set(mediaCount.get() + 1);
                if (mediaCount.get() == 4) {
                    mediaButton.setDisable(true);
                }

                StackPane stackPane = new StackPane();
                stackPane.setStyle("-fx-alignment: TOP_LEFT");
                stackPane.setPrefHeight(USE_COMPUTED_SIZE);
                stackPane.setPrefWidth(USE_COMPUTED_SIZE);
                stackPane.setMaxHeight(USE_PREF_SIZE);
                stackPane.setMaxWidth(USE_PREF_SIZE);
                stackPane.setMinHeight(USE_PREF_SIZE);
                stackPane.setMinWidth(USE_PREF_SIZE);

                stackPane.setUserData(img);

                Button button = new Button();
                button.setText("X");
                button.setStyle("-fx-background-radius: 10em;" +
                                "-fx-min-height: 20px;" +
                                "-fx-min-width: 20px;" +
                                "-fx-max-height: 20px;" +
                                "-fx-max-width: 20px;" +
                                "-fx-font-family: 'Apple Braille';" +
                                "-fx-font-size: 7px;");

                button.setOnAction(event1 -> {
                    tweetGridPane.getChildren().remove(stackPane);
                    stage.sizeToScene();
                    mediaButton.setDisable(false);
                    media.remove((com.manely.ap.project.common.model.Image) stackPane.getUserData());
                    mediaCount.set(mediaCount.get() - 1);
                    if (tweet.getKind().equals(Tweet.Kind.TWEET) && mediaCount.get() == 0) {
                        pollButton.setDisable(false);
                    }
                });

                Image image = new Image(new ByteArrayInputStream(img.getBytes()));
                ImageView imageView = new ImageView();
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setImage(image);

                stackPane.getChildren().addAll(imageView, button);

                int columnIndex = 0, rowIndex = 0;

                if (mediaCount.get() % 2 == 0) {
                    columnIndex = 1;
                }
                if (mediaCount.get() > 2) {
                    rowIndex = 1;
                }

                tweetGridPane.add(stackPane, columnIndex, rowIndex);
                stage.sizeToScene();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int choiceCount = 2;
    private TextField choice1;
    private TextField choice2;
    private TextField choice3 = null;
    private TextField choice4 = null;
    private VBox vBox;
    private Button addButton;
    private Button removeButton;

    private TextField makeChoice(int index) {
        TextField textField = new TextField();
        textField.setMaxHeight(USE_PREF_SIZE);
        textField.setMaxWidth(USE_PREF_SIZE);
        textField.setMinWidth(USE_PREF_SIZE);
        textField.setMinHeight(USE_PREF_SIZE);

        textField.setStyle("-fx-font-family: 'Apple Braille'");

        if (index > 2) {
            textField.setPromptText("Choice " + index + " (Optional)");
        }
        else {
            textField.setPromptText("Choice " + index);
        }

        return textField;
    }

    private Button makeButton() {
        Button button = new Button();
        button.setMaxHeight(USE_PREF_SIZE);
        button.setMaxWidth(USE_PREF_SIZE);
        button.setMinWidth(USE_PREF_SIZE);
        button.setMinHeight(USE_PREF_SIZE);

        button.setStyle("-fx-font-family: 'Apple Braille'");

        return button;
    }

    private VBox addPoll() {
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setMaxHeight(USE_PREF_SIZE);
        vBox.setMaxWidth(USE_PREF_SIZE);
        vBox.setMinWidth(USE_PREF_SIZE);
        vBox.setMinHeight(USE_PREF_SIZE);
        vBox.setSpacing(8);

        tweetTextArea.setPromptText("Ask a question!");
        choice1 = makeChoice(1);
        choice2 = makeChoice(2);
        addButton = makeButton();
        removeButton = makeButton();

        choice1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.isBlank() && !choice2.getText().isBlank() && !tweetTextArea.getText().isBlank()) {
                    tweetButton.setDisable(false);
                }
                else {
                    tweetButton.setDisable(true);
                }
            }
        });

        choice2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.isBlank() && !choice1.getText().isBlank() && !tweetTextArea.getText().isBlank()) {
                    tweetButton.setDisable(false);
                }
                else {
                    tweetButton.setDisable(true);
                }
            }
        });

        addButton.setText("+");
        removeButton.setText("Remove");

        vBox.getChildren().addAll(choice1, choice2, addButton, removeButton);

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                ++choiceCount;
                if (choiceCount == 3) {
                    choice3 = makeChoice(choiceCount);
                    vBox.getChildren().add(2, choice3);
                }
                else {
                    choice4 = makeChoice(choiceCount);
                    vBox.getChildren().add(3, choice4);
                    addButton.setDisable(true);
                }
                stage.sizeToScene();
            }
        });

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                choiceCount = 2;
                isPoll = false;
                mediaButton.setDisable(false);
                choice1 = null;
                choice2 = null;
                choice3 = null;
                choice4 = null;
                tweetVBox.getChildren().remove(vBox);
                stage.sizeToScene();
            }
        });

        return vBox;
    }

    @FXML
    void pollButtonPressed(ActionEvent event) {
        isPoll = true;
        mediaButton.setDisable(true);
        tweetVBox.getChildren().add(addPoll());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.sizeToScene();
    }

    @FXML
    void tweetButtonPressed() {

        if (isPoll) {
            Poll poll = new Poll();
            poll.setQuestion(tweetTextArea.getText());
            poll.setChoice1(choice1.getText());
            poll.setChoice2(choice2.getText());
            if (choice3 != null && !choice3.getText().isBlank()) {
                poll.setChoice3(choice3.getText());
            }
            if (choice4 != null && !choice4.getText().isBlank()) {
                poll.setChoice4(choice4.getText());
            }
            tweet.setPoll(poll);
        }
        else {
            tweet.setImages(media.toArray(new com.manely.ap.project.common.model.Image[0]));
            tweet.setSenderUsername(Data.getUser().getUsername());
        }

        tweet.setText(tweetTextArea.getText());

        String path = null;
        switch (tweet.getKind()) {
            case TWEET -> path = API.TWEET;
            case QUOTE -> path = API.QUOTE;
            case REPLY -> path = API.REPLY;
        }

        if (path != null) {
            HttpCall.post(path, tweet, Object.class,
                new ResponseCallback<>() {
                    @Override
                    public void run() {
                    if (getResponse().isSuccess()) {
                        Scene.gotoHomePage();
                    }
                    else {
                        System.out.println(getResponse().getMessage());
                        System.exit(1);
                    }
                    }
                });
        }
    }

    @FXML
    void cancelButtonPressed() {
        Scene.gotoHomePage();
    }

}
