package com.manely.ap.project.database.tables;

import com.manely.ap.project.database.SQL;
import com.manely.ap.project.common.model.Retweet;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Retweets extends Table {


    public static final String TABLE_NAME = "Retweets";
    private static final String COLUMN_SENDER = "Sender";
    public static final String COLUMN_TWEET_ID = "TweetID";
    private static final String COLUMN_DATE = "Date";


    @Override
    public synchronized void create() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                            "(" +
                            COLUMN_SENDER + " TEXT, " +
                            COLUMN_TWEET_ID + " INTEGER, " +
                            COLUMN_DATE + " BIGINT, " +
                            "FOREIGN KEY(" + COLUMN_SENDER + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_USERNAME + ")," +
                            "FOREIGN KEY(" + COLUMN_TWEET_ID + ") REFERENCES " + Tweets.TABLE_NAME + "(" + Tweets.COLUMN_ID + ") " +
                            ")");
    }

    public synchronized void insert(Retweet retweet) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME +
                        "(" +
                        COLUMN_SENDER + ", " +
                        COLUMN_TWEET_ID + ", " +
                        COLUMN_DATE +
                        ") VALUES (?, ?, ?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, retweet.getSender());
        statement.setInt(2, retweet.getTweet().getId());
        statement.setLong(3, retweet.getDate().getTime());
        statement.executeUpdate();
        statement.close();
        retweet.setId(retweet.getTweet().getId());
        SQL.getPosts().insertRetweet(retweet);
    }
}
