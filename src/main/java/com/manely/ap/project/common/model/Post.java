package com.manely.ap.project.common.model;

import java.io.Serializable;
import java.util.Date;

public abstract class Post implements Serializable {
    private int id;
    private String sender;
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
