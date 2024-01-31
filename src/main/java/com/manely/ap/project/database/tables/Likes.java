package com.manely.ap.project.database.tables;

import com.manely.ap.project.database.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Likes extends Table {
    public static final String TABLE_NAME = "Likes";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_TWEET_ID = "TweetID";

    @Override
    public synchronized void create() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                            "(" +
                            COLUMN_USERNAME + " TEXT, " +
                            COLUMN_TWEET_ID + " INTEGER, " +
                            "FOREIGN KEY(" + COLUMN_USERNAME + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_USERNAME + "), " +
                            "FOREIGN KEY(" + COLUMN_TWEET_ID + ") REFERENCES " + Tweets.TABLE_NAME + "(" + Tweets.COLUMN_ID + ")" +
                            ")");
    }

    public synchronized void insert(String username, int tweetId) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME +
                        "(" +
                        COLUMN_USERNAME + ", " +
                        COLUMN_TWEET_ID +
                        ") VALUES (?, ?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, username);
        statement.setInt(2, tweetId);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized void delete(String username, int tweetId) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME +
                            " WHERE " + COLUMN_USERNAME + "=? AND " +
                            COLUMN_TWEET_ID + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, username);
        statement.setInt(2, tweetId);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized boolean exist(String username, int tweetId) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_USERNAME + "=? AND " +
                        COLUMN_TWEET_ID + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, username);
        statement.setInt(2, tweetId);
        ResultSet result = statement.executeQuery();
        boolean exists = result.next();

        result.close();
        statement.close();
        return exists;
    }

    public synchronized ArrayList<String> select(int tweetId) throws SQLException {
        String query = "SELECT " + COLUMN_USERNAME + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_TWEET_ID + "=" + tweetId;
        Statement statement = SQL.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        ArrayList<String> result = new ArrayList<>();

        while (resultSet.next()) {
            result.add(resultSet.getString(COLUMN_USERNAME));
        }

        return result;
    }

}
