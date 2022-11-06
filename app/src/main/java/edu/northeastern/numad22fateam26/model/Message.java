package edu.northeastern.numad22fateam26.model;

public class Message {
    private String senderId;
    private String receiverId;
    private String sentAt;
    private boolean isRead;
    private String title;
    private String detail;
    private String stickerId;

    public Message(String senderId, String receiverId, String sentAt, boolean isRead, String title, String detail, String stickerId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sentAt = sentAt;
        this.isRead = isRead;
        this.title = title;
        this.detail = detail;
        this.stickerId = stickerId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }
}
