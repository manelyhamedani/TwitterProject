package com.manely.ap.project.client.controller;

import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.Main;
import com.manely.ap.project.client.ResponseCallback;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.client.util.ButtonUtility;
import com.manely.ap.project.client.util.TweetUtility;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;
import com.manely.ap.project.common.model.Tweet.Kind;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.manely.ap.project.client.util.ButtonUtility.setColorButtonImage;

public class Tweet extends VBox {
    private final NumberFormat formatter = NumberFormat.getInstance();
    private Post tweet;
    private Tweet quotedTweet;

    private Image retweetImage;
    private Image coloredRetweetImage;
    private Image likeImage;
    private Image coloredLikeImage;
    private SimpleBooleanProperty likedProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty retweetedProperty = new SimpleBooleanProperty(false);
    private boolean isLiked = false;
    private boolean retweeted = false;

    public void setQuotedTweet() {
        quotedTweet = new Tweet();
        quotedTweet.setTweet(((com.manely.ap.project.common.model.Tweet) tweet).getRefTweet());
        new Scene(quotedTweet);

        Rectangle clip = new Rectangle(300, 250);
        clip.setArcHeight(20);
        clip.setArcHeight(20);

        quoteImageView.setClip(clip);
        SnapshotParameters p = new SnapshotParameters();
        p.setFill(Color.TRANSPARENT);
        Platform.runLater(() -> {
            WritableImage roundedImage = quoteImageView.snapshot(p, null);
            quoteImageView.setClip(null);
            quoteImageView.setImage(roundedImage);
        });

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.WHITE);
        Platform.runLater(() -> {
            WritableImage snapshot = quotedTweet.snapshot(parameters, null);
            quoteImageView.setFitWidth(300);
            quoteImageView.setFitHeight(250);
            quoteImageView.setImage(snapshot);
            quoteImageView.setEffect(new DropShadow(20, Color.BLACK));
            quoteTweetButton.setStyle("-fx-background-color: transparent");
        });

        quoteTweetButton.setOnAction((event) -> TweetUtility.showRefTweet(quotedTweet.getTweet()));
        quoteTweetButton.setOnMouseEntered((event) -> quoteImageView.setEffect(new DropShadow(30, Color.BLACK)));
        quoteTweetButton.setOnMouseExited((event) -> quoteImageView.setEffect(new DropShadow(20, Color.BLACK)));
    }

    public SimpleBooleanProperty likeProperty() {
        return likedProperty;
    }

    public SimpleBooleanProperty retweetedProperty() {
        return retweetedProperty;
    }

    public void retweet() {
        retweeted = true;
        Platform.runLater(() -> {
            int count = Integer.parseInt(retweetCountLabel.getText());
            retweetCountLabel.setText(String.valueOf(++count));
            ((ImageView) retweetButton.getGraphic()).setImage(coloredRetweetImage);
        });
    }

    public void like() {
        isLiked = true;
        Platform.runLater(() -> {
            int count = Integer.parseInt(likeCountLabel.getText());
            likeCountLabel.setText(String.valueOf(++count));
            ((ImageView) likeButton.getGraphic()).setImage(coloredLikeImage);
        });
    }

    public void unlike() {
        isLiked = false;
        Platform.runLater(() -> {
            int count = Integer.parseInt(likeCountLabel.getText());
            likeCountLabel.setText(String.valueOf(--count));
            ((ImageView) likeButton.getGraphic()).setImage(likeImage);
        });    }


    @FXML
    private Button quoteTweetButton;

    @FXML
    private Button avatarButton;

    @FXML
    private Button commentButton;

    @FXML
    private Label commentCountLabel;

    @FXML
    private Button likeButton;

    @FXML
    private Label likeCountLabel;

    @FXML
    private Hyperlink nameLink;

    @FXML
    private Button quoteButton;

    @FXML
    private Label quoteCountLabel;

    @FXML
    private Button retweetButton;

    @FXML
    private Label retweetCountLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private VBox tweetVBox;

    @FXML
    private Hyperlink usernameLink;

    @FXML
    private Text tweetText;

    @FXML
    private GridPane tweetGridPane;

    @FXML
    private HBox commentHBox;

    @FXML
    private HBox retweetHBox;

    @FXML
    private HBox quoteHBox;

    @FXML
    private HBox tweetHBox;

    @FXML
    private HBox retweetSenderHBox;

    @FXML
    private ImageView quoteImageView;

    @FXML
    private HBox replyHBox;

    public void initialize() throws IOException {
       setButtonImages();

       coloredRetweetImage = setColorButtonImage(retweetButton, 0xff39e139);
       coloredLikeImage = setColorButtonImage(likeButton, 0xffe03a3a);

       likeCountLabel.setText("0");
       retweetCountLabel.setText("0");
       quoteCountLabel.setText("0");
       commentCountLabel.setText("0");

    }

    public Post getTweet() {
        return tweet;
    }

    private void setButtonImages() throws MalformedURLException {
        URL comment = new URL("file:/Users/melody/Downloads/comment.png");
        URL retweet = new URL("file:/Users/melody/Downloads/retweet.png");
        URL quote = new URL("file:/Users/melody/Downloads/quote.png");
        URL like = new URL("file:/Users/melody/Downloads/like.png");
        URL avatar = new URL("file:/Users/melody/Downloads/avatar.png");

        Image commentImage = new Image(comment.toString());
        retweetImage = new Image(retweet.toString());
        Image quoteImage = new Image(quote.toString());
        likeImage = new Image(like.toString());
        Image avatarImage = new Image(avatar.toString());

        ((ImageView) commentButton.getGraphic()).setImage(commentImage);
        ((ImageView) retweetButton.getGraphic()).setImage(retweetImage);
        ((ImageView) quoteButton.getGraphic()).setImage(quoteImage);
        ((ImageView) likeButton.getGraphic()).setImage(likeImage);
        ((ImageView) avatarButton.getGraphic()).setImage(avatarImage);
    }

    public Tweet() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("tweet.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRetweet(Retweet retweet) {

        setTweet(retweet.getTweet());
        this.tweet = retweet;

        tweetHBox.setPadding(new Insets(0, 14, 8, 14));
        retweetSenderHBox.setPadding(new Insets(0, 0, 0, 60));

        ImageView imageView = new ImageView();
        imageView.setFitHeight(17);
        imageView.setFitWidth(17);
        imageView.setImage(retweetImage);

        Hyperlink link = new Hyperlink();
        link.setTextFill(Color.BLACK);
        link.setStyle("-fx-border-color: transparent;" +
                        "-fx-font-family: 'Apple Braille'");
        link.setUnderline(false);
        String senderName;
        if (retweet.getSenderUsername().equals(Data.getUser().getUsername())) {
            senderName = "You";
        }
        else {
            senderName = retweet.getSenderName();
        }
        link.setText(senderName + " Retweeted");

        retweetSenderHBox.getChildren().addAll(imageView, link);
    }

    public void setTweet(com.manely.ap.project.common.model.Tweet tweet) {
        this.tweet = tweet;

        if (tweet.getKind().equals(Kind.QUOTE)) {
            setQuotedTweet();
        }
        else if (tweet.getKind().equals(Kind.REPLY)) {
            tweetHBox.setPadding(new Insets(0, 14, 8, 14));
            replyHBox.setPadding(new Insets(0, 0, 0, 60));

            Hyperlink link = new Hyperlink();
            link.setTextFill(Color.DEEPSKYBLUE);
            link.setStyle("-fx-border-color: transparent;" +
                           "-fx-font-family: 'Apple Braille'" );
            link.setUnderline(false);
            link.setText("Reply to >>");

            link.setOnMouseEntered((event) -> link.setUnderline(true));
            link.setOnMouseExited((event) -> link.setUnderline(false));
            link.setOnAction((event) -> TweetUtility.showRefTweet(tweet.getRefTweet()));

            replyHBox.getChildren().add(link);
        }

        nameLink.setText(tweet.getSenderName());
        usernameLink.setText("@" + tweet.getSenderUsername());
        commentCountLabel.setText(formatter.format(tweet.getCommentsCount()));
        retweetCountLabel.setText(formatter.format(tweet.getRetweetsCount()));
        quoteCountLabel.setText(formatter.format(tweet.getQuotesCount()));
        likeCountLabel.setText(formatter.format(tweet.getLikesCount()));

        if (tweet.getLikes().contains(Data.getUser().getUsername())) {
            Platform.runLater(() -> ((ImageView) likeButton.getGraphic()).setImage(coloredLikeImage));
            isLiked = true;
            likedProperty.set(true);
        }

        if (tweet.getRetweets().contains(Data.getUser().getUsername())) {
            Platform.runLater(() -> ((ImageView) retweetButton.getGraphic()).setImage(coloredRetweetImage));
            retweeted = true;
            retweetedProperty.set(true);
        }

        double rightPadding = commentHBox.getPadding().getRight();
        double maxWidth = Math.max(Math.max(commentCountLabel.getWidth(), retweetCountLabel.getWidth()), Math.max(quoteCountLabel.getWidth(), likeCountLabel.getWidth()));
        Insets newInsets = new Insets(0, rightPadding - maxWidth, 0, 0);
        commentHBox.setPadding(newInsets);
        retweetHBox.setPadding(newInsets);
        quoteHBox.setPadding(newInsets);

        tweetText.setText(tweet.getText());
        Date date = tweet.getDate();
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        long diff = currentTime.getTime() - date.getTime();
        long hourMillis = 60 * 60 * 1000;
        long dayMillis = 24 * hourMillis;
        long yearMillis = 365 * dayMillis;
        if (diff < hourMillis) {
            long minuteMillis = 60 * 1000;
            int min = (int) (diff / minuteMillis);
            timeLabel.setText(formatter.format(min) + "m");
        }
        else if (diff < dayMillis) {
            int hour = (int) (diff / hourMillis);
            timeLabel.setText(formatter.format(hour) + "h");
        }
        else if (diff < yearMillis) {
            timeLabel.setText(formatter.format(month) + " " + formatter.format(day));
        }
        else {
            timeLabel.setText(formatter.format(year) + " " + formatter.format(month) + " " + formatter.format(day));
        }

        if (tweet.getSenderAvatar() != null) {
            Image avatar = new Image(new ByteArrayInputStream(tweet.getSenderAvatar().getBytes()));
            ImageView imageView = (ImageView) avatarButton.getGraphic();
            ButtonUtility.setRoundedImageView(imageView, avatar);
        }

        int count = 1;
        int columnIndex = 1;
        int rowIndex = 1;
        if (tweet.getImageCount() > 0) {
            for (com.manely.ap.project.common.model.Image img : tweet.getImages()) {
                if (count % 2 == 0) {
                    columnIndex = 0;
                }
                if (count > 2) {
                    rowIndex = 0;
                }

                Button imageButton = new Button();
                imageButton.setMaxHeight(Double.MAX_VALUE);
                imageButton.setMaxWidth(Double.MAX_VALUE);
                imageButton.setText("");
                imageButton.setStyle("-fx-background-color: transparent");
                imageButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //TODO:handle show image
                    }
                });

                ImageView imageView = new ImageView();
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                Image image = new Image(new ByteArrayInputStream(img.getBytes()));
                imageView.setImage(image);
                imageButton.setGraphic(imageView);

                tweetGridPane.add(imageButton, columnIndex, rowIndex);
                ++count;
            }
        }
    }

    @FXML
    void avatarButtonPressed(ActionEvent event) {

    }

    @FXML
    void commentButtonPressed(ActionEvent event) {
        AddTweet addTweet = new AddTweet();
        addTweet.setTweetKind(Kind.REPLY);
        if (tweet instanceof Retweet) {
            addTweet.setRefTweet(((Retweet) tweet).getTweet());
        }
        else {
            addTweet.setRefTweet((com.manely.ap.project.common.model.Tweet) tweet);
        }
        Scene scene = new Scene(addTweet);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void likeButtonPressed() {
        String path;
        if (isLiked) {
            path = API.UNLIKE;
        }
        else {
            path = API.LIKE;
        }
        HashMap<String, String> query = new HashMap<>();
        query.put("id", String.valueOf(tweet.getId()));
        HttpCall.get(path, query, Object.class,
                new ResponseCallback<>() {
                    @Override
                    public void run() {
                        if (getResponse().isSuccess()) {
                            if (isLiked) {
                                likedProperty.set(false);
                                unlike();
                            }
                            else {
                               likedProperty.set(true);
                               like();
                            }
                        }
                        else {
                            System.out.println(getResponse().getMessage());
                            System.exit(1);
                        }
                    }
                });
    }

    @FXML
    void quoteButtonPressed(ActionEvent event) {
        AddTweet addTweet = new AddTweet();
        addTweet.setTweetKind(Kind.QUOTE);
        if (tweet instanceof Retweet) {
            addTweet.setRefTweet(((Retweet) tweet).getTweet());
        }
        else {
            addTweet.setRefTweet((com.manely.ap.project.common.model.Tweet) tweet);
        }
        Scene scene = new Scene(addTweet);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void retweetButtonPressed() {
        if (!retweeted) {
            HashMap<String, String> query = new HashMap<>();
            query.put("id", String.valueOf(tweet.getId()));
            HttpCall.get(API.RETWEET, query, Object.class,
                    new ResponseCallback<>() {
                        @Override
                        public void run() {
                            if (getResponse().isSuccess()) {
                                retweet();
                                retweetedProperty.set(true);

                            }
                            else {
                                System.out.println(getResponse().getMessage());
                                System.exit(1);
                            }
                        }
                    });
        }

    }

}
