package edu.northeastern.numad22fateam26.model;

public class Notification {
    String uuid;
    String senderName;
    String date;
    String id;
    String message;
    boolean reviewed;

    public Notification() {

    }

    public Notification(String uuid, String senderName, String date, String id, String message) {
        this.uuid = uuid;
        this.senderName = senderName;
        this.date = date;
        this.id = id;
        this.message = message;
        this.reviewed = false;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
