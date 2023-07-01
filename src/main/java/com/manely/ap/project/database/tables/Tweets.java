package com.manely.ap.project.database.tables;

import com.manely.ap.project.common.model.*;
import com.manely.ap.project.database.SQL;
import com.manely.ap.project.filemanager.MediaManager;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Tweets extends Table {
    private static int tweetsCount = -1;

    public static final String TABLE_NAME = "Tweets";
    public static final String COLUMN_ID = "ID";
    private static final String COLUMN_SENDER = "Sender";
    private static final String COLUMN_TEXT = "Text";
    public static final String COLUMN_LIKES = "Likes";
    private static final String COLUMN_RETWEETS = "Retweets";
    private static final String COLUMN_COMMENTS = "Comments";
    private static final String COLUMN_QUOTES = "Quotes";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_QUOTED_TWEET = "QuotedTweet";
    private static final String COLUMN_REPLIED_TWEET = "RepliedTweet";


    @Override
    public synchronized void create() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                            "(" +
                            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_SENDER + " TEXT, " +
                            COLUMN_TEXT + " TEXT, " +
                            COLUMN_LIKES + " INTEGER, " +
                            COLUMN_RETWEETS + " INTEGER, " +
                            COLUMN_COMMENTS + " INTEGER, " +
                            COLUMN_QUOTES + " INTEGER, " +
                            COLUMN_DATE + " BIGINT, " +
                            COLUMN_QUOTED_TWEET + " INTEGER, " +
                            COLUMN_REPLIED_TWEET + " INTEGER, " +
                            "FOREIGN KEY(" + COLUMN_SENDER + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_USERNAME + "), " +
                            "FOREIGN KEY(" + COLUMN_QUOTED_TWEET + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + "), " +
                            "FOREIGN KEY(" + COLUMN_REPLIED_TWEET + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + ") " +
                            ")");
    }

    private void fetchTweetsCount() throws SQLException {
        Statement statement = SQL.getConnection().createStatement();
        ResultSet set = statement.executeQuery("SELECT COUNT() FROM " + TABLE_NAME);
        tweetsCount = set.getInt(1);
    }

    public synchronized void insert(Tweet tweet) throws SQLException {
        if (tweetsCount == -1) {
            fetchTweetsCount();
        }
        String query = "INSERT INTO " + TABLE_NAME +
                        "(" +
                        COLUMN_SENDER + ", " +
                        COLUMN_TEXT + ", " +
                        COLUMN_LIKES + ", " +
                        COLUMN_RETWEETS + ", " +
                        COLUMN_COMMENTS + ", " +
                        COLUMN_QUOTES + ", " +
                        COLUMN_DATE + ", " +
                        COLUMN_QUOTED_TWEET + ", " +
                        COLUMN_REPLIED_TWEET +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, tweet.getSenderUsername());
        statement.setString(2, tweet.getText());
        statement.setInt(3, tweet.getLikesCount());
        statement.setInt(4, tweet.getRetweetsCount());
        statement.setInt(5, tweet.getCommentsCount());
        statement.setInt(6, tweet.getQuotesCount());
        statement.setLong(7, tweet.getDate().getTime());
        if (tweet.getKind().equals(Tweet.Kind.QUOTE)) {
            statement.setInt(8, tweet.getRefTweet().getId());
        }
        else {
            statement.setNull(8, Types.INTEGER);
        }
        if (tweet.getKind().equals(Tweet.Kind.REPLY)) {
            statement.setInt(9, tweet.getRefTweet().getId());
        }
        else {
            statement.setNull(9, Types.INTEGER);
        }
        statement.executeUpdate();
        statement.close();
        ++tweetsCount;
        tweet.setId(tweetsCount);
        SQL.getPosts().insertTweet(tweet);
    }

    public synchronized Post select(int id) throws SQLException, IOException {
        String query = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_ID + "=" + id;
        Statement statement = SQL.getConnection().createStatement();
        ResultSet set = statement.executeQuery(query);

        ArrayList<Post> result = read(set);
        if (result.size() != 1) {
            return null;
        }

        set.close();
        statement.close();
        return result.get(0);
    }

    public synchronized ArrayList<Post> query(String quest) throws SQLException, IOException {
        String query = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_TEXT + " LIKE '%" + quest.trim() + "%'";
        Statement statement = SQL.getConnection().createStatement();
        ResultSet set = statement.executeQuery(query);
        ArrayList<Post> result = read(set);

        set.close();
        statement.close();
        return result;
    }

    public synchronized ArrayList<Post> fetchTweetReplies(int id) throws SQLException, IOException {
        String query = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_REPLIED_TWEET + "=" + id;
        Statement statement = SQL.getConnection().createStatement();
        ResultSet set = statement.executeQuery(query);
        ArrayList<Post> result = read(set);

        set.close();
        statement.close();
        return result;
    }

    private ArrayList<Post> read(ResultSet set) throws SQLException, IOException {
        ArrayList<Post> tweets = new ArrayList<>();

        while (set.next()) {
            int refTweet;
            Tweet tweet = new Tweet();
            if ((refTweet = set.getInt(COLUMN_QUOTED_TWEET)) != 0) {
                tweet.setKind(Tweet.Kind.QUOTE);
                tweet.setRefTweet((Tweet) select(refTweet));
            }
            else if ((refTweet = set.getInt(COLUMN_REPLIED_TWEET)) != 0) {
                tweet.setKind(Tweet.Kind.REPLY);
                tweet.setRefTweet((Tweet) select(refTweet));
            }
            else {
                tweet.setKind(Tweet.Kind.TWEET);
            }
            tweet.setId(set.getInt(COLUMN_ID));
            tweet.setSenderUsername(set.getString(COLUMN_SENDER));
            tweet.setText(set.getString(COLUMN_TEXT));
            tweet.setDate(new Date(set.getLong(COLUMN_DATE)));
            tweet.setLikesCount(set.getInt(COLUMN_LIKES));
            tweet.setCommentsCount(set.getInt(COLUMN_COMMENTS));
            tweet.setQuotesCount(set.getInt(COLUMN_QUOTES));
            tweet.setRetweetsCount(set.getInt(COLUMN_RETWEETS));
            tweet.setLikes(SQL.getLikes().select(tweet.getId()));
            tweet.setRetweets(SQL.getRetweets().select(tweet.getId()));
            User sender = SQL.getUsers().fetchTweetSender(tweet.getSenderUsername());
            tweet.setSenderName(sender.getFirstName() + " " + sender.getLastName());
            tweet.setSenderAvatar(MediaManager.getUserAvatar(sender.getUsername()));
            tweets.add(tweet);
        }

        return tweets;
    }

    public synchronized void retweet(int id) throws SQLException {
        updateUp(id, COLUMN_RETWEETS);
    }

    public synchronized void quote(int id) throws SQLException {
        updateUp(id, COLUMN_QUOTES);
    }

    public synchronized void like(int id) throws SQLException {
        updateUp(id, COLUMN_LIKES);
    }

    public synchronized void unlike(int id) throws SQLException {
        executeUpdate("UPDATE " + TABLE_NAME +
                " SET " + Tweets.COLUMN_LIKES + "=" + Tweets.COLUMN_LIKES + "-1 WHERE " +
                COLUMN_ID + "=" + id);
    }

    public synchronized void comment(int id) throws SQLException {
        updateUp(id, COLUMN_COMMENTS);
    }

    private void updateUp(int id, String column) throws SQLException {
        executeUpdate("UPDATE " + TABLE_NAME +
                " SET " + column + "=" + column + "+1 WHERE " +
                COLUMN_ID + "=" + id);
    }


}
