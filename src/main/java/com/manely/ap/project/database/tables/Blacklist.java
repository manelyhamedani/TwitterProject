package com.manely.ap.project.database.tables;

import com.manely.ap.project.database.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Blacklist extends Table {
    public static final String TABLE_NAME = "Blacklist";
    public static final String COLUMN_BLOCKER = "Blocker";
    public static final String COLUMN_BLOCKED = "Blocked";

    @Override
    public synchronized void create() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                            "(" +
                            COLUMN_BLOCKER + " TEXT, " +
                            COLUMN_BLOCKED + " TEXT, " +
                            "FOREIGN KEY(" + COLUMN_BLOCKER + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_USERNAME + "), " +
                            "FOREIGN KEY(" + COLUMN_BLOCKER + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_USERNAME + ") " +
                            ")"
                            );
    }

    public synchronized void insert(String blockerUsername, String blockedUsername) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME +
                            "(" +
                            COLUMN_BLOCKER + ", " +
                            COLUMN_BLOCKED +
                            ") VALUES (?, ?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, blockerUsername);
        statement.setString(2, blockedUsername);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized void delete(String blockerUsername, String blockedUsername) throws SQLException {
        String query = "DELETE FROM " + TABLE_NAME +
                            " WHERE " + COLUMN_BLOCKER + "=? AND " +
                            COLUMN_BLOCKED + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, blockerUsername);
        statement.setString(2, blockedUsername);
        statement.executeUpdate();
        statement.close();
    }

    public synchronized boolean exists(String blockerUsername, String blockedUsername) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_BLOCKER + "=? AND " +
                        COLUMN_BLOCKED + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, blockerUsername);
        statement.setString(2, blockedUsername);
        ResultSet result = statement.executeQuery();
        boolean exists = result.next();

        result.close();
        statement.close();
        return exists;
    }

    public synchronized ArrayList<String> selectBlockers(String blockedUsername) throws SQLException {
        String query = "SELECT " + COLUMN_BLOCKER + " FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_BLOCKED + "=?";

        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, blockedUsername);
        ResultSet set = statement.executeQuery();

        ArrayList<String> blockers = new ArrayList<>();

        while (set.next()) {
            blockers.add(set.getString(COLUMN_BLOCKER));
        }

        set.close();
        statement.close();
        return blockers;
    }

    public synchronized ArrayList<String> selectBlockedUsers(String blockerUsername) throws SQLException {
        String query = "SELECT " + COLUMN_BLOCKED + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_BLOCKER + "=?";

        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, blockerUsername);
        ResultSet set = statement.executeQuery();

        ArrayList<String> blockers = new ArrayList<>();

        while (set.next()) {
            blockers.add(set.getString(COLUMN_BLOCKED));
        }

        set.close();
        statement.close();
        return blockers;
    }

}
