package edu.northeastern.numad22fateam26.model;

public class StickerHistory {
    String senderName;
    String date;
    String id;
    String message;

    public StickerHistory(String senderName, String date, String id, String message) {
        this.senderName = senderName;
        this.date = date;
        this.id = id;
        this.message = message;
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
}
