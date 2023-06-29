package com.manely.ap.project.database.tables;

import com.manely.ap.project.common.model.Post;
import com.manely.ap.project.common.model.Retweet;
import com.manely.ap.project.common.model.Tweet;
import com.manely.ap.project.common.model.User;
import com.manely.ap.project.database.SQL;
import com.manely.ap.project.filemanager.MediaManager;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Posts extends Table {

    private static final String TABLE_NAME = "Posts";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_TWEET_ID = "TweetID";
    private static final String COLUMN_RETWEET_ID = "RetweetID";
    private static final String COLUMN_SENDER = "Sender";
    private static final String COLUMN_DATE = "Date";

    @Override
    public synchronized void create() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TWEET_ID + " INTEGER, " +
                COLUMN_RETWEET_ID + " INTEGER, " +
                COLUMN_SENDER + " TEXT, " +
                COLUMN_DATE + " BIGINT, " +
                "FOREIGN KEY(" + COLUMN_TWEET_ID + ") REFERENCES " + Tweets.TABLE_NAME + "(" + Tweets.COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_RETWEET_ID + ") REFERENCES " + Retweets.TABLE_NAME + "(" + Retweets.COLUMN_TWEET_ID + "), " +
                "FOREIGN KEY(" + COLUMN_SENDER + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_USERNAME + ")" +
                ")");
    }

    public synchronized void insertTweet(Post post) throws SQLException {
        insert(post, COLUMN_TWEET_ID);
    }

    public synchronized void insertRetweet(Post post) throws SQLException {
        insert(post, COLUMN_RETWEET_ID);
    }

    private void insert(Post post, String column) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME +
                        "(" +
                        column + ", " +
                        COLUMN_SENDER + ", " +
                        COLUMN_DATE +
                        ") VALUES (?, ?, ?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setInt(1, post.getId());
        statement.setString(2, post.getSenderUsername());
        statement.setLong(3, post.getDate().getTime());
        statement.executeUpdate();
        statement.close();
    }

    public synchronized ArrayList<Post> fetchUserPosts(String username, long date) throws SQLException, IOException {
        String query = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_DATE + "<=? AND " +
                        COLUMN_SENDER + "=? " +
                        "ORDER BY " + COLUMN_DATE + " DESC LIMIT 30";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setLong(1, date);
        statement.setString(2, username);
        ResultSet set = statement.executeQuery();

        return readPosts(set);
    }

    public synchronized ArrayList<Post> fetchTimelinePosts(String username, int id) throws SQLException, IOException {
        String limit = "(";
        if (id != -1) {
            limit = TABLE_NAME + "." + COLUMN_ID + "<" + id + " AND (";
        }
        String query = "SELECT DISTINCT " + TABLE_NAME + ".*" + " FROM " + TABLE_NAME +
                        " INNER JOIN " +
                        Follow.TABLE_NAME + ", " + Tweets.TABLE_NAME +
                        " WHERE " +
                        limit +
                        "(" + TABLE_NAME + "." + COLUMN_SENDER + "=" + Follow.TABLE_NAME + "." + Follow.COLUMN_FOLLOWING + " AND " +
                        Follow.TABLE_NAME + "." + Follow.COLUMN_FOLLOWER + "=?) OR " +
                        "(" + TABLE_NAME + "." + COLUMN_SENDER + "=?) OR " +
                        "(" + Tweets.TABLE_NAME + "." + Tweets.COLUMN_ID + "=" + TABLE_NAME + "." + COLUMN_TWEET_ID + " AND " +
                        Tweets.TABLE_NAME + "." + Tweets.COLUMN_LIKES + ">=10)" +
                        ") ORDER BY " + TABLE_NAME + "." + COLUMN_DATE + " DESC LIMIT 30";

        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, username);
        ResultSet set = statement.executeQuery();

        return readPosts(set);
    }

    private ArrayList<Post> readPosts(ResultSet set) throws SQLException, IOException {
        ArrayList<Post> result = new ArrayList<>();
        Post post = null;

        while (set.next()) {
            int tweetId;
            if ((tweetId = set.getInt(COLUMN_TWEET_ID)) != 0) {
                post = SQL.getTweets().select(tweetId);
            }
            else if ((tweetId = set.getInt(COLUMN_RETWEET_ID)) != 0) {
                post = new Retweet();
                post.setId(tweetId);
                post.setSenderUsername(set.getString(COLUMN_SENDER));
                post.setDate(new Date(set.getLong(COLUMN_DATE)));
                post.setPostID(set.getInt(COLUMN_ID));
                fetchSenderInfo(post);
                ((Retweet) post).setTweet((Tweet) SQL.getTweets().select(tweetId));
            }
            if (post != null) {
                result.add(post);
            }

        }
        set.close();
        return result;
    }

    private void fetchSenderInfo(Post post) throws IOException, SQLException {
        User sender = SQL.getUsers().fetchTweetSender(post.getSenderUsername());
        post.setSenderName(sender.getFirstName() + " " + sender.getLastName());
        post.setSenderAvatar(MediaManager.getUserAvatar(sender.getUsername()));
    }
}
