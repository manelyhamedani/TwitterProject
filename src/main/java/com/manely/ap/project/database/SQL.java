package com.manely.ap.project.database;

import com.manely.ap.project.database.tables.*;
import org.sqlite.SQLiteConfig;
import com.manely.ap.project.common.model.Error;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQL {
    private static final File DATABASE = new File("database.db");
    private static Connection connection;
    private static final Users users = new Users();
    private static final Follow follows = new Follow();
    private static final Tweets tweets = new Tweets();
    private static final Retweets retweets = new Retweets();
    private static final Posts posts = new Posts();
    private static final Likes likes = new Likes();
    private static final Blacklist blacklist = new Blacklist();

    private SQL() {
    }

    public static void connect() throws SQLException {
        if (connection != null)
            return;
        connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE.getAbsolutePath());
        if (connection == null)
            throw new SQLException();
        createTables();
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null)
            connect();
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection == null)
            return;
       connection.close();
    }

    private static void createTables() throws SQLException {
        users.create();
        follows.create();
        tweets.create();
        retweets.create();
        posts.create();
        likes.create();
        blacklist.create();
    }

    public static Blacklist getBlacklist() {
        return blacklist;
    }

    public static Likes getLikes() {
        return likes;
    }

    public static Tweets getTweets() {
        return tweets;
    }

    public static Retweets getRetweets() {
        return retweets;
    }

    public static Posts getPosts() {
        return posts;
    }

    public static Users getUsers() {
        return users;
    }

    public static Follow getFollows() {
        return follows;
    }

    public static Error getError(String errMsg) {
        if (errMsg.contains("SQLITE_CONSTRAINT_UNIQUE")) {
            if (errMsg.contains("Users.Username")) {
                return Error.DUPLICATE_USERNAME;
            }
            if (errMsg.contains("Users.Email")) {
                return Error.DUPLICATE_EMAIL;
            }
            if (errMsg.contains("Users.PhoneNumber")) {
                return Error.DUPLICATE_PHONE;
            }
        }
        if (errMsg.contains("SQLITE_CONSTRAINT_NOTNULL")) {
            if (errMsg.contains("Users.Username")) {
                return Error.NULL_USERNAME;
            }
            if (errMsg.contains("Users.Password")) {
                return Error.NULL_PASS;
            }
            if (errMsg.contains("Users.Firstname")) {
                return Error.NULL_FIRSTNAME;
            }
            if (errMsg.contains("Users.Lastname")) {
                return Error.NULL_LASTNAME;
            }
        }
        if (errMsg.contains("SQLITE_CONSTRAINT_FOREIGNKEY")) {
            return Error.ID_NOT_FOUND;
        }
        return Error.SQL_ERROR;
    }
}
