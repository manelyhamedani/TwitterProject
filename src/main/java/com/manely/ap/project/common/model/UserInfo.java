package com.manely.ap.project.common.model;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.Serializable;


public class UserInfo implements Serializable {
    private String bio;
    private String location;
    private String website;
    private static final transient int MAX_BIO_LENGTH = 160;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        if (bio != null) {
            this.bio = bio.trim();
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location != null) {
            this.location = location.trim();
        }
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        if (website != null) {
            this.website = website.trim();
        }
    }

    public boolean validateBio() {
        if (bio != null)
            return (bio.length() <= MAX_BIO_LENGTH);
        return true;
    }

    public boolean validateWebsite() {
        if (website != null) {
            UrlValidator urlValidator = new UrlValidator();
            return urlValidator.isValid(website);
        }
        return true;
    }
}
