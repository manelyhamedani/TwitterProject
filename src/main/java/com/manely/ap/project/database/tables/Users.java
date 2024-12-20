package com.manely.ap.project.database.tables;

import com.manely.ap.project.database.SQL;
import com.manely.ap.project.common.model.*;
import com.manely.ap.project.filemanager.MediaManager;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Users extends Table {
    private static int usersCount = -1;

    public static final String TABLE_NAME = "Users";
    private static final String COLUMN_ID = "ID";
    public static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_FIRSTNAME = "Firstname";
    private static final String COLUMN_LASTNAME = "Lastname";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    private static final String COLUMN_COUNTRY = "Country";
    private static final String COLUMN_BIRTHDATE = "Birthdate";
    private static final String COLUMN_DATE_ADDED = "DateAdded";
    private static final String COLUMN_LAST_DATE_MODIFIED = "LastDateModified";
    private static final String COLUMN_BIO = "Bio";
    private static final String COLUMN_LOCATION = "Location";
    private static final String COLUMN_WEBSITE = "Website";

    private enum Kind {
        USER,
        PROFILE,
        TWEET_SENDER
    }

    @Override
    public synchronized void create() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                            "(" +
                            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                            COLUMN_PASSWORD + " TEXT NOT NULL, " +
                            COLUMN_FIRSTNAME + " TEXT NOT NULL, " +
                            COLUMN_LASTNAME + " TEXT NOT NULL, " +
                            COLUMN_EMAIL + " TEXT UNIQUE, " +
                            COLUMN_PHONE_NUMBER + " TEXT UNIQUE, " +
                            COLUMN_COUNTRY + " TEXT, " +
                            COLUMN_BIRTHDATE + " BIGINT, " +
                            COLUMN_DATE_ADDED + " BIGINT, " +
                            COLUMN_LAST_DATE_MODIFIED + " BIGINT, " +
                            COLUMN_BIO + " TEXT, " +
                            COLUMN_LOCATION + " TEXT, " +
                            COLUMN_WEBSITE + " TEXT " +
                            ")");
    }

    private void fetchUsersCount() throws SQLException {
        Statement statement = SQL.getConnection().createStatement();
        ResultSet set = statement.executeQuery("SELECT COUNT() FROM " + TABLE_NAME);
        usersCount = set.getInt(1);
    }

    public synchronized void insert(User user) throws SQLException {
        if (usersCount == -1) {
            fetchUsersCount();
        }
        String query = "INSERT INTO " + TABLE_NAME +
                        "(" +
                        COLUMN_USERNAME + ", " +
                        COLUMN_PASSWORD + ", " +
                        COLUMN_FIRSTNAME + ", " +
                        COLUMN_LASTNAME + ", " +
                        COLUMN_EMAIL + ", " +
                        COLUMN_PHONE_NUMBER + ", " +
                        COLUMN_COUNTRY + ", " +
                        COLUMN_BIRTHDATE + ", " +
                        COLUMN_DATE_ADDED + ", " +
                        COLUMN_LAST_DATE_MODIFIED +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());
        statement.setString(5, user.getEmail());
        statement.setString(6, user.getPhoneNumber());
        statement.setString(7, user.getCountry());
        Date birthdate = user.getBirthDate();
        if (birthdate == null) {
            statement.setNull(8, Types.BIGINT);
        }
        else {
            statement.setLong(8, user.getBirthDate().getTime());
        }
        statement.setLong(9, user.getDateAdded().getTime());
        statement.setLong(10, user.getLastDateModified().getTime());
        statement.executeUpdate();
        statement.close();
        ++usersCount;
        user.setId(usersCount);
    }

    public synchronized User select(String username) throws SQLException {
        return selectAs(Kind.USER, username);
    }

    public synchronized ArrayList<User> query(String quest) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_USERNAME + " LIKE " + "'%" + quest.trim() + "%'" + " OR " +
                    COLUMN_FIRSTNAME + " LIKE " + "'%" + quest.trim() + "%'" + " OR " +
                    COLUMN_LASTNAME + " LIKE " + "'%" + quest.trim() + "%'";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        ResultSet set = statement.executeQuery();
        return readAs(Kind.PROFILE, set);
    }

    public synchronized User fetchTweetSender(String username) throws SQLException {
        return selectAs(Kind.TWEET_SENDER, username);
    }

    public synchronized ArrayList<User> fetchProfiles(ArrayList<String> usernames) throws SQLException {
        ArrayList<User> profiles = new ArrayList<>();
        if (usernames == null || usernames.size() == 0) {
            return profiles;
        }
        for (String username : usernames) {
            profiles.add(selectAs(Kind.PROFILE, username));
        }
        return profiles;
    }

    private User selectAs(Kind kind, String username) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USERNAME + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, username.trim());

        ResultSet set = statement.executeQuery();

        ArrayList<User> result = readAs(kind, set);

        if (result.size() != 1) {
            return null;
        }
        set.close();
        statement.close();
        return result.get(0);
    }

    private ArrayList<User> readAs(Kind kind, ResultSet set) throws SQLException {
        ArrayList<User> profiles = new ArrayList<>();
        User profile;
        while (set.next()) {
            profile = new User();
            profile.setUsername(set.getString(COLUMN_USERNAME));
            profile.setFirstName(set.getString(COLUMN_FIRSTNAME));
            profile.setLastName(set.getString(COLUMN_LASTNAME));
            if (kind.equals(Kind.TWEET_SENDER)) {
                profiles.add(profile);
                continue;
            }
            profile.setBirthDate(new Date(set.getLong(COLUMN_BIRTHDATE)));
            profile.setDateAdded(new Date(set.getLong(COLUMN_DATE_ADDED)));
            UserInfo info = new UserInfo();
            info.setBio(set.getString(COLUMN_BIO));
            info.setLocation(set.getString(COLUMN_LOCATION));
            info.setWebsite(set.getString(COLUMN_WEBSITE));
            profile.setInfo(info);
            profile.setFollowers(getFollowers(profile.getUsername()));
            profile.setFollowings(getFollowing(profile.getUsername()));
            if (kind.equals(Kind.USER)) {
                profile.setPassword(set.getString(COLUMN_PASSWORD));
                profile.setEmail(set.getString(COLUMN_EMAIL));
                profile.setPhoneNumber(set.getString(COLUMN_PHONE_NUMBER));
                profile.setCountry(set.getString(COLUMN_COUNTRY));
                profile.setLastDateModified(new Date(set.getLong(COLUMN_LAST_DATE_MODIFIED)));
                profile.setVotes(SQL.getVote().select(profile.getUsername()));
                profile.setBlocked(SQL.getBlacklist().selectBlockedUsers(profile.getUsername()));
            }
            profiles.add(profile);
        }
        return profiles;
    }

    private ArrayList<String> getFollowers(String username) throws SQLException {
        return SQL.getFollows().selectFollowers(username);
    }

    private ArrayList<String> getFollowing(String username) throws SQLException {
        return SQL.getFollows().selectFollowings(username);
    }

    public synchronized void updateInfo(String username, User user) throws SQLException {
        String query = "UPDATE " + TABLE_NAME +
                        " SET " +
                        COLUMN_FIRSTNAME + "=?, " +
                        COLUMN_LASTNAME + "=?, " +
                        COLUMN_BIO + "=?, " +
                        COLUMN_LOCATION + "=?, " +
                        COLUMN_WEBSITE + "=?, " +
                        COLUMN_LAST_DATE_MODIFIED + "=? " +
                        "WHERE " + COLUMN_USERNAME + "=?";
        long currentTime = System.currentTimeMillis();
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setString(3, user.getInfo().getBio());
        statement.setString(4, user.getInfo().getLocation());
        statement.setString(5, user.getInfo().getWebsite());
        statement.setLong(6, currentTime);
        statement.setString(7, username);
        statement.executeUpdate();
        statement.close();
    }




}
