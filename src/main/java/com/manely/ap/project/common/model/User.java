package com.manely.ap.project.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String country;
    private Date birthDate;
    private Date dateAdded;
    private Date lastDateModified;
    private Image avatar;
    private Image header;
    private UserInfo info;
    private String jwt;
    private ArrayList<String> followers = new ArrayList<>();
    private ArrayList<String> followings = new ArrayList<>();
    private HashMap<Integer, Integer> votes = new HashMap<>();
    private ArrayList<String> blocked = new ArrayList<>();

    public ArrayList<String> getBlocked() {
        return blocked;
    }

    public void setBlocked(ArrayList<String> blocked) {
        this.blocked = blocked;
    }

    public HashMap<Integer, Integer> getVotes() {
        return votes;
    }

    public void setVotes(HashMap<Integer, Integer> votes) {
        this.votes = votes;
    }

    public ArrayList<String > getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<String> followings) {
        this.followings = followings;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        if (jwt != null) {
            this.jwt = jwt.trim();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollowersCount() {
        return followers.size();
    }

    public int getFollowingCount() {
        return followings.size();
    }

    public UserInfo getInfo() {
        return info;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }

    public Image getHeader() {
        return header;
    }

    public void setHeader(Image header) {
        this.header = header;
    }

    public void setUsername(String username) {
        if (username != null) {
            this.username = username.trim();
        }
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = password.trim();
        }
    }

    public void setFirstName(String firstName) {
        if (firstName != null) {
            this.firstName = firstName.trim();
        }
    }

    public void setLastName(String lastName) {
        if (lastName != null) {
            this.lastName = lastName.trim();
        }
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email.trim();
        }
        else {
            this.email = null;
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber.trim();
        }
        else {
            this.phoneNumber = null;
        }
    }

    public void setCountry(String country) {
        if (country != null) {
            this.country = country.trim();
        }
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setLastDateModified(Date lastDateModified) {
        this.lastDateModified = lastDateModified;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getLastDateModified() {
        return lastDateModified;
    }
}
