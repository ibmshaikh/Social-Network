package com.example.ibrahim.snc.models;


import java.util.Date;

/**
 * Created by ibrahim on 24/3/18.
 */

public class postclass {


    public postclass(){


    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getUploaderID() {
        return UploaderID;
    }

    public void setUploaderID(String uploaderID) {
        UploaderID = uploaderID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    private String ImageURL,UploaderID,Description;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date timestamp;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(String userProfile) {
        UserProfile = userProfile;
    }

    private String UserName,UserProfile;

}
