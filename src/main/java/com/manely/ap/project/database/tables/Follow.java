package com.manely.ap.project.database.tables;

import com.manely.ap.project.database.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Follow extends Table {
    public static final String TABLE_NAME = "Follow";
    public static final String COLUMN_FOLLOWER = "Follower";
    public static final String COLUMN_FOLLOWING = "Following";

    @Override
    public synchronized void create() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                            "(" +
                            COLUMN_FOLLOWER + " TEXT, " +
                            COLUMN_FOLLOWING + " TEXT, " +
                            "FOREIGN KEY(" + COLUMN_FOLLOWER + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_USERNAME + ")," +
                            "FOREIGN KEY(" + COLUMN_FOLLOWING + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_USERNAME + ")" +
                            ")");
    }

    public synchronized void insert(String followerUsername, String followingUsername) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME +
                        "(" +
                        COLUMN_FOLLOWER + ", " +
                        COLUMN_FOLLOWING +
                        ") VALUES (?, ?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, followerUsername);
        statement.setString(2, followingUsername);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized void deleteUnilateral(String followerUsername, String followingUsername) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_FOLLOWER + "=? AND " +
                        COLUMN_FOLLOWING + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, followerUsername);
        statement.setString(2, followingUsername);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized void deleteBilateral(String username1, String username2) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME +
                " WHERE (" + COLUMN_FOLLOWER + "=? OR " +
                COLUMN_FOLLOWER + "=?) AND (" +
                COLUMN_FOLLOWING + "=? OR " +
                COLUMN_FOLLOWING + "=?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, username1);
        statement.setString(2, username2);
        statement.setString(3, username1);
        statement.setString(4, username2);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized boolean exists(String followerUsername, String followingUsername) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_FOLLOWER + "=? AND " +
                COLUMN_FOLLOWING + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, followerUsername);
        statement.setString(2, followingUsername);
        ResultSet result = statement.executeQuery();

        boolean exists = result.next();

        result.close();
        statement.close();
        return exists;
    }

    public synchronized ArrayList<String> selectFollowers(String followingUsername) throws SQLException {
        return select(followingUsername, COLUMN_FOLLOWER, COLUMN_FOLLOWING);
    }

    public synchronized ArrayList<String> selectFollowings(String followerUsername) throws SQLException {
        return select(followerUsername, COLUMN_FOLLOWING, COLUMN_FOLLOWER);
    }

    private ArrayList<String> select(String username, String targetColumn, String sourceColumn) throws SQLException {
        String query = "SELECT " + targetColumn + " FROM " + TABLE_NAME +
                        " WHERE " + sourceColumn + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, username);
        ResultSet set = statement.executeQuery();
        ArrayList<String> result = new ArrayList<>();
        while (set.next()) {
            result.add(set.getString(targetColumn));
        }
        set.close();
        statement.close();
        return result;
    }

}

