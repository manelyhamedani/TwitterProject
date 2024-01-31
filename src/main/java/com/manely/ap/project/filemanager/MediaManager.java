package com.manely.ap.project.filemanager;

import com.manely.ap.project.common.model.Image;
import com.manely.ap.project.database.SQL;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class MediaManager {
    private static final String MEDIA_DIR_PATH = "media";
    private static final String USERS_DIR_PATH = "media/users";
    private static final String TWEETS_DIR_PATH = "media/tweets";

    private MediaManager() {
    }

    public static boolean setUp() {
        File media = new File(MEDIA_DIR_PATH);
        File users = new File(USERS_DIR_PATH);
        File tweets = new File(TWEETS_DIR_PATH);

        if (!media.exists()) {
            if (!media.mkdir())
                return false;
        }

        if (!users.exists()) {
            if (!users.mkdir())
                return false;
        }

        if (!tweets.exists()) {
            return tweets.mkdir();
        }

        return true;
    }

    public static boolean addUser(String username) {
        File user = new File(USERS_DIR_PATH + "/" + username);
        return user.mkdir();
    }

    public static void addUserMedia(String username, Image image) throws IOException {
        String dirPath = USERS_DIR_PATH + "/" + username;

        File usrDir = new File(dirPath);
        if (!usrDir.exists()) {
            throw new IOException("User dir not found!");
        }
        String[] avatarFile = usrDir.list((dir, name) -> name.contains("avatar"));
        String[]  headerFile = usrDir.list((dir, name) -> name.contains("header"));

        String imgPath;
        if (image.getKind() == Image.KIND.AVATAR) {
            imgPath = dirPath + "/avatar." + image.getFormat();
            if (avatarFile != null && avatarFile.length == 1) {
                if (!new File(dirPath + "/" + avatarFile[0]).delete()) {
                    return;
                }
            }
        }
        else {
            imgPath = dirPath + "/header." + image.getFormat();
            if (headerFile != null && headerFile.length == 1) {
                if (!new File(dirPath + "/" + headerFile[0]).delete()) {
                    return;
                }
            }
        }

        FileOutputStream fos = new FileOutputStream(imgPath);
        fos.write(image.getBytes());
    }

    public static void addTweetMedia(int id, Image[] images) throws IOException {
        String dirPath = TWEETS_DIR_PATH + "/" + id;
        File tweet = new File(dirPath);
        if (!tweet.mkdir())
            throw new IOException();

        if (images == null || images.length == 0) {
            return;
        }

        int counter = 1;
        for (Image img : images) {
            String imgPath = dirPath + "/img" + counter + "." + img.getFormat();

            FileOutputStream fos = new FileOutputStream(imgPath);
            fos.write(img.getBytes());

            ++counter;
        }
    }

    public static Image getUserAvatar(String username) throws IOException {
        String dirPath = USERS_DIR_PATH + "/" + username;
        File usrDir = new File(dirPath);
        if (!usrDir.exists()) {
            throw new IOException("User dir not found!");
        }
        String[] avatarFile = usrDir.list((dir, name) -> name.contains("avatar"));
        if (avatarFile == null || avatarFile.length != 1) {
            return null;
        }
        String format = avatarFile[0].split("\\.")[1];
        String avatarPath = usrDir + "/" + avatarFile[0];

        FileInputStream fis = new FileInputStream(avatarPath);

        return new Image(format, fis.readAllBytes(), Image.KIND.AVATAR);
    }

    public static Image getUserHeader(String username) throws IOException {
        String dirPath = USERS_DIR_PATH + "/" + username;
        File usrDir = new File(dirPath);
        if (!usrDir.exists()) {
            throw new IOException("User dir not found!");
        }
        String[] headerFile = usrDir.list((dir, name) -> name.contains("header"));
        if (headerFile == null || headerFile.length != 1) {
            return null;
        }
        String format = headerFile[0].split("\\.")[1];
        String headerPath = usrDir + "/" + headerFile[0];

        FileInputStream fis = new FileInputStream(headerPath);

        return new Image(format, fis.readAllBytes(), Image.KIND.HEADER);
    }

    public static Image[] getTweetMedia(int id) throws IOException {
        String dirPath = TWEETS_DIR_PATH + "/" + id;
        File tweetDir = new File(dirPath);
        if (!tweetDir.exists())
            throw new IOException("Tweet dir not found!");
        File[] imgFiles = tweetDir.listFiles();
        if (imgFiles == null || imgFiles.length == 0)
            return null;

        ArrayList<Image> images = new ArrayList<>();
        FileInputStream fis;
        for (File imgFile : imgFiles) {
            fis = new FileInputStream(imgFile);
            String format = imgFile.getName().split("\\.")[1];
            images.add(new Image(format, fis.readAllBytes(), Image.KIND.TWEET));
        }
        return images.toArray(new Image[0]);
    }

}
