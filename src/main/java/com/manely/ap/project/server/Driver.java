package com.manely.ap.project.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.concurrent.Executors;

import com.manely.ap.project.common.API;
import com.manely.ap.project.filemanager.MediaManager;
import com.manely.ap.project.database.SQL;

public class Driver {
    public static void main( String[] args ) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(API.PORT), 0);
        server.setExecutor(Executors.newCachedThreadPool());

        server.createContext(API.LOGIN, HttpHandler::handleLogin);
        server.createContext(API.SIGNUP, HttpHandler::handleSignUp);
        server.createContext(API.SET_USER_IMAGE, HttpHandler::handleSetUserImage);
        server.createContext(API.SET_USER_INFO, HttpHandler::handleSetUserInfo);
        server.createContext(API.FOLLOW, HttpHandler::handleFollow);
        server.createContext(API.UNFOLLOW, HttpHandler::handleUnfollow);
        server.createContext(API.LIST_FOLLOWERS, HttpHandler::handleListFollowers);
        server.createContext(API.LIST_FOLLOWING, HttpHandler::handleListFollowing);
        server.createContext(API.SEARCH, HttpHandler::handleSearch);
        server.createContext(API.TWEET, HttpHandler::handleTweet);
        server.createContext(API.QUOTE, HttpHandler::handleQuote);
        server.createContext(API.RETWEET, HttpHandler::handleRetweet);
        server.createContext(API.LIKE, HttpHandler::handleLike);
        server.createContext(API.UNLIKE, HttpHandler::handleUnlike);
        server.createContext(API.REPLY, HttpHandler::handleReply);
        server.createContext(API.BLOCK, HttpHandler::handleBlock);
        server.createContext(API.UNBLOCK, HttpHandler::handleUnblock);
        server.createContext(API.TIMELINE, HttpHandler::handleTimeline);
        server.createContext(API.FILTER, HttpHandler::handleFilter);
        server.start();

        if (!MediaManager.setUp())
            throw new IOException("Media Manager Crashed!");

        try {
            SQL.connect();
        }
        catch (SQLException e) {
            //TODO : handle
            e.printStackTrace();
        }
    }
}
