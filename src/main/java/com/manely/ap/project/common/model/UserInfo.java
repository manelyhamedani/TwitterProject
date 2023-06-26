package com.manely.ap.project.common.model;

import org.apache.commons.validator.routines.UrlValidator;


public class UserInfo {
    private String bio;
    private String location;
    private String website;
    private static final int MAX_BIO_LENGTH = 160;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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
