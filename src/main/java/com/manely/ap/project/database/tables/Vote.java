package com.manely.ap.project.database.tables;

import com.manely.ap.project.database.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Vote extends Table {

    private static final String TABLE_NAME = "Vote";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_VOTER = "Voter";
    private static final String COLUMN_POLL = "Poll";
    private static final String COLUMN_CHOICE = "Choice";

    public synchronized void create() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_VOTER + " TEXT, " +
                        COLUMN_POLL + " INTEGER, " +
                        COLUMN_CHOICE + " INTEGER, " +
                        "FOREIGN KEY(" + COLUMN_VOTER + ") REFERENCES " + Users.TABLE_NAME + "(" + Users.COLUMN_USERNAME + "), " +
                        "FOREIGN KEY(" + COLUMN_POLL + ") REFERENCES " + Polls.TABLE_NAME + "(" + Polls.COLUMN_ID + ") " +
                        ")");
    }


    public synchronized void insert(String voter, int pollId, int choice) throws SQLException {
        String query = "INSERT INTO " + TABLE_NAME +
                "(" +
                COLUMN_VOTER + ", " +
                COLUMN_POLL + ", " +
                COLUMN_CHOICE +
                ") VALUES (?, ?, ?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, voter);
        statement.setInt(2, pollId);
        statement.setInt(3, choice);
        statement.executeUpdate();
    }

    public synchronized HashMap<Integer, Integer> select(String voter) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_VOTER + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, voter);
        ResultSet set = statement.executeQuery();

        HashMap<Integer, Integer> result = new HashMap<>();

        while (set.next()) {
            result.put(set.getInt(COLUMN_POLL), set.getInt(COLUMN_CHOICE));
        }

        return result;
    }

}
