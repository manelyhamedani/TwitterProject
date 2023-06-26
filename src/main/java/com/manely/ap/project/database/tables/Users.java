package com.manely.ap.project.database.tables;

import com.manely.ap.project.database.SQL;
import com.manely.ap.project.common.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        statement.setLong(8, user.getBirthDate().getTime());
        statement.setLong(9, user.getDateAdded().getTime());
        statement.setLong(10, user.getLastDateModified().getTime());
        statement.executeUpdate();
        statement.close();
        ++usersCount;
        user.setId(usersCount);
    }

    public synchronized User select(String username) throws SQLException {
        return selectAs(true, username);
    }

    public synchronized ArrayList<User> query(String quest) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_USERNAME + " LIKE " + "'%" + quest.trim() + "%'" + " OR " +
                    COLUMN_FIRSTNAME + " LIKE " + "'%" + quest.trim() + "%'" + " OR " +
                    COLUMN_LASTNAME + " LIKE " + "'%" + quest.trim() + "%'";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        ResultSet set = statement.executeQuery();
        return readAs(false, set);
    }

    public synchronized User fetchProfile(String username) throws SQLException {
        return selectAs(false, username);
    }

    public synchronized ArrayList<User> fetchProfiles(ArrayList<String> usernames) throws SQLException {
        ArrayList<User> profiles = new ArrayList<>();
        if (usernames == null || usernames.size() == 0) {
            return profiles;
        }
        for (String username : usernames) {
            profiles.add(selectAs(false, username));
        }
        return profiles;
    }

    private User selectAs(boolean user, String username) throws SQLException {
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USERNAME + "=?";
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, username.trim());

        ResultSet set = statement.executeQuery();

        ArrayList<User> result = readAs(user, set);

        if (result.size() != 1) {
            return null;
        }
        set.close();
        statement.close();
        return result.get(0);
    }

    private ArrayList<User> readAs(boolean user, ResultSet set) throws SQLException {
        ArrayList<User> profiles = new ArrayList<>();
        User profile;
        while (set.next()) {
            profile = new User();
            profile.setUsername(set.getString(COLUMN_USERNAME));
            profile.setFirstName(set.getString(COLUMN_FIRSTNAME));
            profile.setLastName(set.getString(COLUMN_LASTNAME));
            profile.setBirthDate(new Date(set.getLong(COLUMN_BIRTHDATE)));
            profile.setDateAdded(new Date(set.getLong(COLUMN_DATE_ADDED)));
            UserInfo info = new UserInfo();
            info.setBio(set.getString(COLUMN_BIO));
            info.setLocation(set.getString(COLUMN_LOCATION));
            info.setWebsite(set.getString(COLUMN_WEBSITE));
            profile.setInfo(info);
            profile.setFollowersCount(getFollowersCount(profile.getUsername()));
            profile.setFollowingCount(getFollowingCount(profile.getUsername()));
            if (user) {
                profile.setId(set.getInt(COLUMN_ID));
                profile.setPassword(set.getString(COLUMN_PASSWORD));
                profile.setEmail(set.getString(COLUMN_EMAIL));
                profile.setPhoneNumber(set.getString(COLUMN_PHONE_NUMBER));
                profile.setCountry(set.getString(COLUMN_COUNTRY));
                profile.setLastDateModified(new Date(set.getLong(COLUMN_LAST_DATE_MODIFIED)));
            }
            profiles.add(profile);
        }
        return profiles;
    }

    private int getFollowersCount(String username) throws SQLException {
        return SQL.getFollows().selectFollowersCount(username);
    }

    private int getFollowingCount(String username) throws SQLException {
        return SQL.getFollows().selectFollowingCount(username);
    }

    public synchronized void updateInfo(String username, UserInfo info) throws SQLException {
        String query = "UPDATE " + TABLE_NAME +
                        " SET " + COLUMN_BIO + "=?, " +
                        COLUMN_LOCATION + "=?, " +
                        COLUMN_WEBSITE + "=?, " +
                        COLUMN_LAST_DATE_MODIFIED + "=? " +
                        "WHERE " + COLUMN_USERNAME + "=?";
        long currentTime = System.currentTimeMillis();
        PreparedStatement statement = SQL.getConnection().prepareStatement(query);
        statement.setString(1, info.getBio());
        statement.setString(2, info.getLocation());
        statement.setString(3, info.getWebsite());
        statement.setLong(4, currentTime);
        statement.setString(5, username);
        statement.executeUpdate();
        statement.close();
    }




}
