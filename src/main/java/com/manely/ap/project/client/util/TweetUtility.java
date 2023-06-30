package com.manely.ap.project.client.util;

import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.client.HttpCall;
import com.manely.ap.project.client.ResponseCallback;
import com.manely.ap.project.client.controller.HomePage;
import com.manely.ap.project.client.controller.Scene;
import com.manely.ap.project.client.controller.Tweet;
import com.manely.ap.project.client.model.Data;
import com.manely.ap.project.common.API;
import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;


public class TweetUtility {
    public enum Kind {
        TIMELINE, PROFILE, SEARCH
    }

    public static void setUp(ListView<Tweet> list, ObservableList<Tweet> tweets, Kind kind, String data) {

        ScrollBar bar = (ScrollBar) list.lookup(".scroll-bar:vertical");
        bar.valueProperty().addListener((observableValue, ov, nv) -> {

            HashMap<String, String> query = new HashMap<>();
            Type type = new TypeToken<ArrayList<Post>>(){}.getType();

            boolean flag = false;
            if (nv.equals(bar.getMax())) {
                flag = true;
            }
            else if (!nv.equals(bar.getMin())){
                return;
            }

            String path = null;

            switch (kind) {
                case TIMELINE -> {
                    path = API.TIMELINE;
                    if (flag) {
                        query.put("id", String.valueOf(Data.getEarliestTimelinePost()));
                    }
                }
                case PROFILE -> {
                    path = API.FETCH_USER_POSTS;
                    query.put("username", data);
                    if (flag) {
                        query.put("id", String.valueOf(Data.getEarliestProfPost()));
                    }
                }
                case SEARCH -> {
                    path = API.FILTER;
                    if (flag) {
//                        query.put("id", String.valueOf(Data.getEarliestSearchPost()));
                    }
                }
            }

            if (path != null) {
                HttpCall.get(path, query, type, new FetchTweetResponseCallback<>(list, tweets));
            }

        });

    }

    public static boolean contains(ObservableList<Tweet> tweets, int id) {
        for (Tweet t : tweets) {
            if (t.getTweet().getPostID() == id) {
                return true;
            }
        }
        return false;
    }

    public static class FetchTweetResponseCallback<T> extends ResponseCallback<T> {
        private ObservableList<Tweet> tweets;
        private ListView<Tweet> listView;

        public FetchTweetResponseCallback(ListView<Tweet> listView, ObservableList<Tweet> tweets) {
            this.tweets = tweets;
            this.listView = listView;
        }

        @Override
        public void run() {
            if (getResponse().isSuccess()) {
                ArrayList<Post> posts = (ArrayList<Post>) getResponse().getContent();
                for (Post post : posts) {
                    if (contains(tweets, post.getPostID())) {
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
                                Platform.runLater(() -> Scene.gotoHomePage(HomePage.getScene()));
                            }
                        });

                    }
                    Data.addTimelinePost(post.getPostID());
                }
                Platform.runLater(() -> {
                    tweets.sort((o1, o2) -> {
                                long id1 = o1.getTweet().getPostID();
                                long id2 = o2.getTweet().getPostID();
                                return Long.compare(id1, id2) * -1;
                    });
                    TweetUtility.setRefs(tweets);
                    listView.setItems(tweets);
                });
            }
            else {
                System.out.println(getResponse().getMessage());
            }
        }
    }

    public static void setRefs(ObservableList<Tweet> tweets) {

        for (Tweet t : tweets) {
            Post post = t.getTweet();

            if (post instanceof Retweet) {
                Tweet ref = getRefTweet(tweets, ((Retweet) post).getTweet().getId());
                setBindings(t, ref);
            }

        }
    }

    public static void showRefTweet(Post refTweet) {
        ListView<Tweet> listView = HomePage.getCurrentList();
        ObservableList<Tweet> tweets = HomePage.getCurrentTweets();

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

    private static Tweet getRefTweet(ObservableList<Tweet> tweets, int tweetId) {
        for (Tweet t : tweets) {
            Post post = t.getTweet();

            if (!(post instanceof Retweet) && post.getId() == tweetId) {
                return t;
            }
        }
        return null;
    }
}
