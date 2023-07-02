package com.manely.ap.project.database.tables;

import com.manely.ap.project.common.model.Poll;
import com.manely.ap.project.common.model.Tweet;
import com.manely.ap.project.database.SQL;

import java.sql.*;

public class Polls extends Table {
    private static int pollCount = -1;

    public static final String TABLE_NAME = "Poll";
    public static final String COLUMN_ID = "ID";
    private static final String COLUMN_QUESTION = "Question";
    private static final String COLUMN_CHOICE_1 = "Choice1";
    private static final String COLUMN_CHOICE_1_COUNT = "Choice1Count";
    private static final String COLUMN_CHOICE_2 = "Choice2";
    private static final String COLUMN_CHOICE_2_COUNT = "Choice2Count";
    private static final String COLUMN_CHOICE_3 = "Choice3";
    private static final String COLUMN_CHOICE_3_COUNT = "Choice3Count";
    private static final String COLUMN_CHOICE_4 = "Choice4";
    private static final String COLUMN_CHOICE_4_COUNT = "Choice4Count";

    public synchronized void create() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_QUESTION + " TEXT, " +
                        COLUMN_CHOICE_1 + " TEXT, " +
                        COLUMN_CHOICE_1_COUNT + " INTEGER, " +
                        COLUMN_CHOICE_2 + " TEXT, " +
                        COLUMN_CHOICE_2_COUNT + " INTEGER, " +
                        COLUMN_CHOICE_3 + " TEXT, " +
                        COLUMN_CHOICE_3_COUNT + " INTEGER, " +
                        COLUMN_CHOICE_4 + " TEXT, " +
                        COLUMN_CHOICE_4_COUNT + " INTEGER " +
                        ")");
    }

    private void fetchPollCount() throws SQLException {
        Statement statement = SQL.getConnection().createStatement();
        ResultSet set = statement.executeQuery("SELECT COUNT() FROM " + TABLE_NAME);
        pollCount = set.getInt(1);
    }

    public synchronized void insert(Poll poll, Tweet tweet) throws SQLException {
        if (pollCount == -1) {
            fetchPollCount();
        }
        String query = "INSERT INTO " + TABLE_NAME +
                "(" +
                COLUMN_QUESTION + ", " +
                COLUMN_CHOICE_1 + ", " +
                COLUMN_CHOICE_1_COUNT + ", " +
                COLUMN_CHOICE_2 + ", " +
                COLUMN_CHOICE_2_COUNT + ", " +
                COLUMN_CHOICE_3 + ", " +
                COLUMN_CHOICE_3_COUNT + ", " +
                COLUMN_CHOICE_4 + ", " +
                COLUMN_CHOICE_4_COUNT +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, poll.getQuestion());
        statement.setString(2, poll.getChoice1());
        statement.setInt(3, 0);
        statement.setString(4, poll.getChoice2());
        statement.setInt(5, 0);
        statement.setString(6, poll.getChoice3());
        statement.setInt(7, 0);
        statement.setString(8, poll.getChoice4());
        statement.setInt(9, 0);

        ++pollCount;
        tweet.setPollId(pollCount);

        statement.executeUpdate();
        statement.close();
    }

    public synchronized Poll select(int id) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NAME +
                        " WHERE " + COLUMN_ID + "=" + id;
        Statement statement = SQL.getConnection().createStatement();
        ResultSet set = statement.executeQuery(query);
        if (!set.next()) {
            return null;
        }

        Poll poll = new Poll();
        poll.setId(id);
        poll.setQuestion(set.getString(COLUMN_QUESTION));
        poll.setChoice1(set.getString(COLUMN_CHOICE_1));
        poll.setChoice1Count(set.getInt(COLUMN_CHOICE_1_COUNT));
        poll.setChoice2(set.getString(COLUMN_CHOICE_2));
        poll.setChoice2Count(set.getInt(COLUMN_CHOICE_2_COUNT));
        poll.setChoice3(set.getString(COLUMN_CHOICE_3));
        poll.setChoice3Count(set.getInt(COLUMN_CHOICE_3_COUNT));
        poll.setChoice4(set.getString(COLUMN_CHOICE_4));
        poll.setChoice4Count(set.getInt(COLUMN_CHOICE_4_COUNT));

        set.close();
        statement.close();
        return poll;
    }

    public synchronized void vote(int id, int choice) throws SQLException {
        String column;
        switch (choice) {
            case 1 -> column = COLUMN_CHOICE_1_COUNT;
            case 2 -> column = COLUMN_CHOICE_2_COUNT;
            case 3 -> column = COLUMN_CHOICE_3_COUNT;
            default -> column = COLUMN_CHOICE_4_COUNT;
        }

        executeUpdate("UPDATE " + TABLE_NAME +
                " SET " + column + "=" + column + "+1 WHERE " +
                COLUMN_ID + "=" + id);
    }
}
