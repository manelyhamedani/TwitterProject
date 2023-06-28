package com.manely.ap.project.common.model;

import java.io.Serializable;
import java.util.Date;

public abstract class Post implements Serializable {
    private int id;
    private String senderUsername;
    private String senderName;
    private Image senderAvatar;
    private Date date;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Image getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(Image senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
