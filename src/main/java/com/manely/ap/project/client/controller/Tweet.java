package com.manely.ap.project.client.controller;

import com.manely.ap.project.client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import static com.manely.ap.project.client.util.ButtonUtility.setColorButtonImage;

public class Tweet extends HBox {
    private final NumberFormat formatter = NumberFormat.getInstance();
    private com.manely.ap.project.common.model.Tweet tweet;

    private Image retweetImage;
    private Image coloredRetweetImage;
    private Image likeImage;
    private Image coloredLikeImage;
    private boolean liked = false;
    private boolean retweeted = false;

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

    public void initialize() throws IOException {
       setButtonImages();

       coloredRetweetImage = setColorButtonImage(retweetButton);
       coloredLikeImage = setColorButtonImage(likeButton);

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

    public void setTweet(com.manely.ap.project.common.model.Tweet tweet) {
        this.tweet = tweet;

        nameLink.setText(tweet.getSenderName());
        usernameLink.setText(tweet.getSenderUsername());
        commentCountLabel.setText(formatter.format(tweet.getCommentsCount()));
        retweetCountLabel.setText(formatter.format(tweet.getRetweetsCount()));
        quoteCountLabel.setText(formatter.format(tweet.getQuotesCount()));
        likeCountLabel.setText(formatter.format(tweet.getLikesCount()));

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
            ((ImageView) avatarButton.getGraphic()).setImage(avatar);
        }

        int count = 1;
        int columnIndex = 0;
        int rowIndex = 0;
        if (tweet.getImageCount() > 0) {
            for (com.manely.ap.project.common.model.Image img : tweet.getImages()) {
                if (count % 2 == 0) {
                    columnIndex = 1;
                }
                if ((double) count / 2 > 1) {
                    rowIndex = 1;
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

    }

    @FXML
    void likeButtonPressed(ActionEvent event) {
        if (liked) {

        }
        else {

        }
    }

    @FXML
    void nameLinkOnMouseMoved(MouseEvent event) {

    }

    @FXML
    void quoteButtonPressed(ActionEvent event) {

    }

    @FXML
    void retweetButtonPressed(ActionEvent event) {
        if (retweeted) {

        }
        else {

        }
    }

    @FXML
    void usernameLinkOnMouseMoved(MouseEvent event) {

    }

}
