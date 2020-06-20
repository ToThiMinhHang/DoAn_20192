package com.hang.doan.readbooks.models;

public class Notification {
    private String id;
    private String message;
    private String fromUserID;
    private long timestamp;
    private String type;
    private String storyID;
    private String fromUserImageURL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStoryID() {
        return storyID;
    }

    public void setStoryID(String storyID) {
        this.storyID = storyID;
    }

    public String getFromUserImageURL() {
        return fromUserImageURL;
    }

    public void setFromUserImageURL(String fromUserImageURL) {
        this.fromUserImageURL = fromUserImageURL;
    }
}
