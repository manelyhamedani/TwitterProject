package com.manely.ap.project.client.util;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.ResponseCallback;
import com.manely.ap.project.client.controller.HomePage;
import com.manely.ap.project.client.controller.Scene;
import com.manely.ap.project.client.controller.Tweet;
import com.manely.ap.project.client.controller.TweetCell;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class TweetUtility {
    private static ObservableList<Tweet> tweets;
    private static ListView<Tweet> listView;

    public static void setUp(ListView<Tweet> list, ObservableList<Tweet> tweetsList) {
        listView = list;
        tweets = tweetsList;

        ScrollBar bar = (ScrollBar) list.lookup(".scroll-bar:vertical");
        bar.valueProperty().addListener((observableValue, ov, nv) -> {
            if (nv.equals(bar.getMin())) {

                HashMap<String, String> query = new HashMap<>();

                Type type = new TypeToken<ArrayList<Post>>(){}.getType();

                HttpCall.get(API.TIMELINE, query,
                        type, new TimelineResponseCallback<>());
            }
            else if (nv.equals(bar.getMax())) {

                HashMap<String, String> query = new HashMap<>();
                query.put("id", String.valueOf(Data.getEarliestTimelinePost()));

                Type type = new TypeToken<ArrayList<Post>>(){}.getType();

                HttpCall.get(API.TIMELINE, query,
                        type, new TimelineResponseCallback<>());
            }
        });

    }

    public static boolean contains(int id) {
        for (Tweet t : tweets) {
            if (t.getTweet().getPostID() == id) {
                return true;
            }
        }
        return false;
    }

    public static class TimelineResponseCallback<T> extends ResponseCallback<T> {
            @Override
            public void run() {
                if (getResponse().isSuccess()) {
                    ArrayList<Post> posts = (ArrayList<Post>) getResponse().getContent();
                    for (Post post : posts) {
                        if (contains(post.getPostID())) {
                            continue;
                        }
                        if (post instanceof Retweet) {
                            Tweet retweet = new Tweet();
                            retweet.setRetweet((Retweet) post);
                            tweets.add(retweet);
                        }

                        else if (post instanceof com.manely.ap.project.common.model.Tweet) {
                            Tweet tweet = new Tweet();
                            tweet.setTweet((com.manely.ap.project.common.model.Tweet) post);
                            tweets.add(tweet);

                            tweet.retweetedProperty().addListener((observableValue, oldValue, newValue) -> {
                                if (newValue) {
                                    Platform.runLater(() -> {
                                        Scene.changeScene("home-page.fxml");
                                    });
                                }
                            });

                        }
                        Data.addTimelinePost(post.getPostID());
                    }
                    Platform.runLater(() -> {
                        tweets.sort((o1, o2) -> {
                                    long id1 = o1.getTweet().getDate().getTime();
                                    long id2 = o2.getTweet().getDate().getTime();
                                    return Long.compare(id1, id2) * -1;
                        });
                        TweetUtility.setRefs();
                        listView.setItems(tweets);
                    });
                }
                else {
                    System.out.println(getResponse().getMessage());
                }
            }
    }

    public static void setRefs() {

        for (Tweet t : TweetUtility.tweets) {
            Post post = t.getTweet();

            if (post instanceof Retweet) {
                Tweet ref = getRefTweet(((Retweet) post).getTweet().getId());
                setBindings(t, ref);
            }

        }
    }

    public static void showRefTweet(Post refTweet) {
        int index = 0;

        for (Tweet t : tweets) {
            Post post = t.getTweet();

            if (!(post instanceof Retweet) && post.equals(refTweet)) {
                int finalIndex = index;
                new Thread(() -> {
                    Platform.runLater(() -> {
                        listView.scrollTo(finalIndex);
                        listView.getItems().get(finalIndex).setStyle("-fx-background-color: #9b9b9b");
                    });
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException ignore) {

                    }
                    Platform.runLater(() -> listView.getItems().get(finalIndex).setStyle("-fx-background-color: white"));
                }).start();
            }

            ++index;
        }
    }

    private static void setBindings(Tweet t, Tweet ref) {
        if (ref != null) {
            t.likeProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    ref.like();
                }
                else {
                    ref.unlike();
                }
            });

            ref.likeProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    t.like();
                }
                else {
                    t.unlike();
                }
            });

            t.retweetedProperty().addListener((ObservableValue, oldValue, newValue) -> {
                if (newValue) {
                    ref.retweet();
                }
            });

            ref.retweetedProperty().addListener((ObservableValue, oldValue, newValue) -> {
                if (newValue) {
                    t.retweet();
                }
            });
        }
    }

    private static Tweet getRefTweet(int tweetId) {
        for (Tweet t : tweets) {
            Post post = t.getTweet();

            if (!(post instanceof Retweet) && post.getId() == tweetId) {
                return t;
            }
        }
        return null;
    }
}
