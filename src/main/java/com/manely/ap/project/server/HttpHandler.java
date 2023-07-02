package com.manely.ap.project.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.common.model.*;
import com.manely.ap.project.common.model.Error;
import com.manely.ap.project.database.SQL;
import com.manely.ap.project.filemanager.MediaManager;
import com.manely.ap.project.server.jwt.JWebToken;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpHandler {

    static void handleLogin(HttpExchange exchange) {
        if (validateMethod("GET", exchange)) {
            try {
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                String[] validQuery = {"username", "password"};
                if (!validateRequestQuery(validQuery, query.keySet())) {
                    throw new IllegalArgumentException();
                }
                User user = SQL.getUsers().select(query.get("username"));
                if (user == null) {
                    response(exchange, 401, Error.UNAUTHORIZED.toString(), false, null, null);
                }
                else if (!user.getPassword().equals(query.get("password"))) {
                    response(exchange, 403, Error.WRONG_PASS.toString(), false, null, null);
                }
                else {
                    user.setAvatar(MediaManager.getUserAvatar(user.getUsername()));
                    user.setHeader(MediaManager.getUserHeader(user.getUsername()));
                    String jwt = JWebToken.generate(user.getUsername(), user.getId(), "user");
                    user.setJwt(jwt);
                    response(exchange, 200, "OK", true, user, User.class);
                }
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleSignUp(HttpExchange exchange) {
        if (validateMethod("POST", exchange)) {
            Error err = Error.UNUSUAL;
            int errCode = 400;
            try {
                User user = getRequestBody(exchange, User.class);
                if (user == null) {
                    throw new IllegalArgumentException();
                }
                if (user.getEmail() == null && user.getPhoneNumber() == null) {
                    err = Error.NULL_EMAIL_PHONE;
                    errCode = 401;
                    throw new IllegalArgumentException();
                }

                if (user.getPassword() != null) {
                    if (user.getPassword().length() < 8 || !user.getPassword().matches("[a-zA-Z]+")) {
                        err = Error.INVALID_PASS;
                        errCode = 402;
                        throw new IllegalArgumentException();
                    }
                }
                if (user.getEmail() != null && !EmailValidator.getInstance().isValid(user.getEmail())) {
                    err = Error.INVALID_EMAIL;
                    errCode = 403;
                    throw new IllegalArgumentException();
                }
                Date now = new Date();
                user.setDateAdded(now);
                user.setLastDateModified(now);
                SQL.getUsers().insert(user);
                if (!MediaManager.addUser(user.getUsername())) {
                    response(exchange, 500, Error.CANNOT_SAVE_IMAGE.toString(), false, null, null);
                }
                else {
                    response(exchange, 200, "OK", true, null, null);
                }
            }
            catch (IllegalArgumentException e) {
                response(exchange, errCode, err.toString(), false, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleSetUserImage(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("POST", exchange)) {
            try {
                String username = JWebToken.getPayload(jwt).getSub();
                Image image = getRequestBody(exchange, Image.class);
                MediaManager.addUserMedia(username, image);
                response(exchange, 200, "OK", true, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleSetUserInfo(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("POST", exchange)) {
            Error err = Error.UNUSUAL;
            try {
                String username = JWebToken.getPayload(jwt).getSub();
                UserInfo userInfo = getRequestBody(exchange, UserInfo.class);
                if (!userInfo.validateBio()) {
                    err = Error.BIO_TOO_LONG;
                    throw new IllegalArgumentException();
                }
                if (!userInfo.validateWebsite()) {
                    err = Error.INVALID_URL;
                    throw new IllegalArgumentException();
                }
                SQL.getUsers().updateInfo(username, userInfo);
                response(exchange, 200, "OK", true, null, null);
            }
            catch (IllegalArgumentException e) {
                response(exchange, 400, err.toString(), false, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleFollow(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                String followerUsername = JWebToken.getPayload(jwt).getSub();
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("username")) {
                    throw new IllegalArgumentException();
                }
                String followingUsername = query.get("username");
                if (followerUsername.equals(followingUsername) || SQL.getFollows().exists(followerUsername, followingUsername)) {
                    throw new IllegalArgumentException(Error.ILLEGAL_FOLLOW.toString());
                }
                SQL.getFollows().insert(followerUsername, followingUsername);
                response(exchange, 200, "OK", true, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleUnfollow(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                String followerUsername = JWebToken.getPayload(jwt).getSub();
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("username")) {
                    throw new IllegalArgumentException();
                }
                String followingUsername = query.get("username");
                if (!SQL.getFollows().exists(followerUsername, followingUsername)) {
                    throw new IllegalArgumentException(Error.ILLEGAL_UNFOLLOW.toString());
                }
                SQL.getFollows().deleteUnilateral(followerUsername, followingUsername);
                response(exchange, 200, "OK", true, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleListFollowers(HttpExchange exchange) {
        if (getJWT(exchange) != null && validateMethod("GET", exchange)) {
            try {
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("username")) {
                    throw new IllegalArgumentException();
                }
                String username = query.get("username");
                ArrayList<String> followerIds = SQL.getFollows().selectFollowers(username);
                ArrayList<User> followers = SQL.getUsers().fetchProfiles(followerIds);
                fetchUsersImages(followers);
                Type contentType = new TypeToken<ArrayList<User>>(){}.getType();
                response(exchange, 200, "OK", true, followers, contentType);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleListFollowing(HttpExchange exchange) {
        if (getJWT(exchange) != null && validateMethod("GET", exchange)) {
            try {
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("username")) {
                    throw new IllegalArgumentException();
                }
                String username = query.get("username");
                ArrayList<String> followingsId = SQL.getFollows().selectFollowings(username);
                ArrayList<User> following = SQL.getUsers().fetchProfiles(followingsId);
                fetchUsersImages(following);
                Type contentType = new TypeToken<ArrayList<User>>(){}.getType();
                response(exchange, 200, "OK", true, following, contentType);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleSearch(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
           try {
               String username = JWebToken.getPayload(jwt).getSub();
               HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
               if (!query.containsKey("quest")) {
                   throw new IllegalArgumentException();
               }
               String quest = query.get("quest");
               ArrayList<User> result = SQL.getUsers().query(quest);
               ArrayList<String> blockers = SQL.getBlacklist().selectBlockers(username);
               result = filterBlockerUsers(result, blockers);
               fetchUsersImages(result);
               Type contentType = new TypeToken<ArrayList<User>>(){}.getType();
               response(exchange, 200, "OK", true, result, contentType);
           }
           catch (Exception e) {
               response(exchange, 400, e.getMessage(), false, null, null);
           }
        }
    }

    private static ArrayList<User> filterBlockerUsers(ArrayList<User> users, ArrayList<String > blockers) {
        ArrayList<User> result = new ArrayList<>();
        for (User user : users) {
            if (!blockers.contains(user.getUsername())) {
                result.add(user);
            }
        }
        return result;
    }

    static void handleVote(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                String username = JWebToken.getPayload(jwt).getSub();
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("id") || !query.containsKey("choice")) {
                    throw new IllegalArgumentException();
                }
                int pollId = Integer.parseInt(query.get("id"));
                int choice = Integer.parseInt(query.get("choice"));

                SQL.getPolls().vote(pollId, choice);
                Poll poll = SQL.getPolls().select(pollId);
                SQL.getVote().insert(username, pollId, choice);
                Type contentType = new TypeToken<Poll>(){}.getType();

                response(exchange, 200, "OK", true, poll, contentType);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleTweet(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("POST", exchange)) {
            Error err = Error.UNUSUAL;
            try {
                String senderUsername = JWebToken.getPayload(jwt).getSub();
                Tweet tweet = getRequestBody(exchange, Tweet.class);
                if ((err = tweet.validateTweet()) != null) {
                    throw new IllegalArgumentException();
                }
                err = Error.UNUSUAL;
                tweet.setSenderUsername(senderUsername);
                long currentTime = System.currentTimeMillis();
                tweet.setDate(new Date(currentTime));
                if (tweet.getPoll() != null) {
                    SQL.getPolls().insert(tweet.getPoll(), tweet);
                }
                SQL.getTweets().insert(tweet);
                MediaManager.addTweetMedia(tweet.getId(), tweet.getImages());
                response(exchange, 200, "OK", true, null, null);
            }
            catch (IllegalArgumentException e) {
                response(exchange, 400 , err.toString(), false, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleQuote(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("POST", exchange)) {
            Error err = Error.UNUSUAL;
            try {
                String sender = JWebToken.getPayload(jwt).getSub();
                Tweet quote = getRequestBody(exchange, Tweet.class);
                if (quote.getRefTweet() == null) {
                    err = Error.NULL_QUOTE;
                    throw new IllegalArgumentException();
                }
                quote.setSenderUsername(sender);
                long currentTime = System.currentTimeMillis();
                quote.setDate(new Date(currentTime));
                SQL.getTweets().insert(quote);
                MediaManager.addTweetMedia(quote.getId(), quote.getImages());
                SQL.getTweets().quote(quote.getRefTweet().getId());
                response(exchange, 200, "OK", true, null, null);
            }
            catch (IllegalArgumentException e) {
                response(exchange, 400 , err.toString(), false, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleRetweet(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                String sender = JWebToken.getPayload(jwt).getSub();

                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("id")) {
                    throw new IllegalArgumentException();
                }
                Retweet retweet = new Retweet();

                retweet.setSenderUsername(sender);

                retweet.setId(Integer.parseInt(query.get("id")));

                long currentTime = System.currentTimeMillis();
                retweet.setDate(new Date(currentTime));

                SQL.getRetweets().insert(retweet);
                SQL.getTweets().retweet(retweet.getId());
                response(exchange, 200, "OK", true, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleLike(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("id")) {
                    throw new IllegalArgumentException();
                }
                int tweetId = Integer.parseInt(query.get("id"));
                String username = JWebToken.getPayload(jwt).getSub();
                if (SQL.getLikes().exist(username, tweetId)) {
                    throw new IllegalArgumentException(Error.ILLEGAL_LIKE.toString());
                }
                SQL.getTweets().like(tweetId);
                SQL.getLikes().insert(username, tweetId);
                response(exchange, 200, "OK", true, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleUnlike(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("id")) {
                    throw new IllegalArgumentException();
                }
                int tweetId = Integer.parseInt(query.get("id"));
                String username = JWebToken.getPayload(jwt).getSub();
                if (!SQL.getLikes().exist(username, tweetId)) {
                    throw new IllegalArgumentException(Error.ILLEGAL_UNLIKE.toString());
                }
                SQL.getTweets().unlike(tweetId);
                SQL.getLikes().delete(username, tweetId);
                response(exchange, 200, "OK", true, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleReply(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("POST", exchange)) {
            Error err = Error.UNUSUAL;
            try {
                String sender = JWebToken.getPayload(jwt).getSub();
                Tweet reply = getRequestBody(exchange, Tweet.class);
                if (reply.getRefTweet() == null) {
                    err = Error.NULL_REPLY;
                    throw new IllegalArgumentException();
                }
                reply.setSenderUsername(sender);
                long currentTime = System.currentTimeMillis();
                reply.setDate(new Date(currentTime));
                SQL.getTweets().insert(reply);
                MediaManager.addTweetMedia(reply.getId(), reply.getImages());
                SQL.getTweets().comment(reply.getRefTweet().getId());
                response(exchange, 200, "OK", true, null, null);
            }
            catch (IllegalArgumentException e) {
                response(exchange, 400 , err.toString(), false, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleBlock(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                String blockerUsername = JWebToken.getPayload(jwt).getSub();
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("username")) {
                    throw new IllegalArgumentException();
                }
                String blockedUsername = query.get("username");
                if (SQL.getBlacklist().exists(blockerUsername, blockedUsername)) {
                    throw new IllegalArgumentException(Error.ILLEGAL_BLOCK.toString());
                }
                SQL.getBlacklist().insert(blockerUsername, blockedUsername);
                SQL.getFollows().deleteBilateral(blockerUsername, blockedUsername);
                response(exchange, 200, "OK", true, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleUnblock(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                String blockerUsername = JWebToken.getPayload(jwt).getSub();
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("username")) {
                    throw new IllegalArgumentException();
                }
                String blockedUsername = query.get("username");
                if (!SQL.getBlacklist().exists(blockerUsername, blockedUsername)) {
                    throw new IllegalArgumentException(Error.ILLEGAL_UNBLOCK.toString());
                }
                SQL.getBlacklist().delete(blockerUsername, blockedUsername);
                response(exchange, 200, "OK", true, null, null);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleFetchUserPosts(HttpExchange exchange) {
        if (getJWT(exchange) != null && validateMethod("GET", exchange)) {
            try {
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                String username;
                if (!query.containsKey("username")) {
                    throw new IllegalArgumentException();
                }
                username = query.get("username");
                ArrayList<Post> posts = SQL.getPosts().fetchUserPosts(username);
                if (posts.size() != 0) {
                     ArrayList<String> blockers = SQL.getBlacklist().selectBlockers(username);
                     posts = filterBlockedPosts(posts, blockers);
                     fetchPostsMedia(posts);
                }
                Type contentType = new TypeToken<ArrayList<Post>>(){}.getType();
                response(exchange, 200, "OK", true, posts, contentType);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleTimeline(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                String username = JWebToken.getPayload(jwt).getSub();
                ArrayList<Post> posts = SQL.getPosts().fetchTimelinePosts(username);
                if (posts.size() != 0) {
                    ArrayList<String> blockers = SQL.getBlacklist().selectBlockers(username);
                    posts = filterBlockedPosts(posts, blockers);
                    fetchPostsMedia(posts);
                }
                Type contentType = new TypeToken<ArrayList<Post>>(){}.getType();
                response(exchange, 200, "OK", true, posts, contentType);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleFilter(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                String username = JWebToken.getPayload(jwt).getSub();
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("quest")) {
                    throw new IllegalArgumentException();
                }
                String quest = query.get("quest");
                ArrayList<Post> tweets = SQL.getTweets().query(quest);
                if (tweets.size() != 0) {
                    ArrayList<String> blockers = SQL.getBlacklist().selectBlockers(username);
                    tweets = filterBlockedPosts(tweets, blockers);
                    fetchPostsMedia(tweets);
                }
                Type contentType = new TypeToken<ArrayList<Post>>(){}.getType();
                response(exchange, 200, "OK", true, tweets, contentType);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    static void handleFetchTweetReplies(HttpExchange exchange) {
        String jwt;
        if ((jwt = getJWT(exchange)) != null && validateMethod("GET", exchange)) {
            try {
                String username = JWebToken.getPayload(jwt).getSub();
                HashMap<String, String> query = parseQuery(exchange.getRequestURI().getQuery());
                if (!query.containsKey("id")) {
                    throw new IllegalArgumentException();
                }
                int id = Integer.parseInt(query.get("id"));
                ArrayList<Post> tweets = SQL.getTweets().fetchTweetReplies(id);
                if (tweets.size() != 0) {
                    ArrayList<String> blockers = SQL.getBlacklist().selectBlockers(username);
                    tweets = filterBlockedPosts(tweets, blockers);
                    fetchPostsMedia(tweets);
                }
                Type contentType = new TypeToken<ArrayList<Post>>(){}.getType();
                response(exchange, 200, "OK", true, tweets, contentType);
            }
            catch (Exception e) {
                response(exchange, 400, e.getMessage(), false, null, null);
            }
        }
    }

    private static void fetchPostMedia(Post post) throws IOException {
        Post refPost = null;

        if (post == null) {
            return;
        }

        if (post instanceof Retweet) {
            refPost = ((Retweet) post).getTweet();
        }

        else if (post instanceof Tweet){
            if (((Tweet) post).getImages() == null)
                ((Tweet) post).setImages(MediaManager.getTweetMedia(post.getId()));
            if (!(((Tweet) post).getKind().equals(Tweet.Kind.TWEET))) {
                refPost = ((Tweet) post).getRefTweet();
            }
        }

        fetchPostMedia(refPost);
    }

    private static void fetchPostsMedia(ArrayList<Post> posts) throws IOException {
        for (Post post : posts) {
            fetchPostMedia(post);
        }
    }

    private static boolean filterPost(Post post, ArrayList<String> blockers) {
        Post refPost = null;

        if (post == null) {
            return true;
        }

        if (post instanceof Retweet) {
            refPost = ((Retweet) post).getTweet();
        }

        else if (!(((Tweet) post).getKind().equals(Tweet.Kind.TWEET))) {
            refPost = ((Tweet) post).getRefTweet();
        }

        if (!blockers.contains(post.getSenderUsername())) {
            return filterPost(refPost, blockers);
        }

        return false;
    }

    private static ArrayList<Post> filterBlockedPosts(ArrayList<Post> posts, ArrayList<String> blockers) {
        ArrayList<Post> result = new ArrayList<>();

        for (Post post : posts) {
            if (filterPost(post, blockers)) {
                result.add(post);
            }
        }

        return result;
    }

    private static void fetchUsersImages(ArrayList<User> users) throws IOException {
        for (User user : users) {
            user.setAvatar(MediaManager.getUserAvatar(user.getUsername()));
            user.setHeader(MediaManager.getUserHeader(user.getUsername()));
        }
    }

    private static String getJWT(HttpExchange exchange) {
        String jwt = exchange.getRequestHeaders().getFirst("Authorization");
        if (!JWebToken.isValid(jwt)) {
            response(exchange, 401, Error.INVALID_JWT.toString(), false, null, null);
            return null;
        }
        return jwt;
    }

    private static <T> T getRequestBody(HttpExchange exchange, Class<T> clazz) {
        return (new Gson()).fromJson(new InputStreamReader(exchange.getRequestBody()), clazz);
    }

    private static boolean validateRequestQuery(String[] expected, Set<String> actual) {
        for (String s : expected) {
            if (!actual.contains(s))
                return false;
        }
        return true;
    }

    private static HashMap<String, String> parseQuery(String query) {
        HashMap<String, String> parsedQuery = new HashMap<>();
        if (query == null) {
            return parsedQuery;
        }

        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                entry[1] = URLDecoder.decode(entry[1], StandardCharsets.UTF_8);
                parsedQuery.put(entry[0], entry[1]);
            }
            else{
                parsedQuery.put(entry[0], "");
            }
        }
        return parsedQuery;
    }

    private static boolean validateMethod(String method, HttpExchange exchange) {
       try {
           HttpMethod.valueOf(exchange.getRequestMethod());
       }
       catch (IllegalArgumentException e) {
           response(exchange, 501, "Not Implemented", false, null, null);
           return false;
       }
       if (!(method.equals(exchange.getRequestMethod()))) {
           response(exchange, 401, "Not Allowed", false, null, null);
           return false;
       }
       return true;
    }

    private static <T> void response(HttpExchange exchange, int status, String msg, boolean success, T content, Type contentType) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeHierarchyAdapter(Post.class, new PostAdapter());
            Type type;
            if (content == null) {
                type = new TypeToken<HttpResponse<Object>>(){}.getType();
            }
            else {
                type = TypeToken.getParameterized(HttpResponse.class, contentType).getType();
            }
            String response = gsonBuilder.create().toJson(new HttpResponse<>(status, msg, success, content), type);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
