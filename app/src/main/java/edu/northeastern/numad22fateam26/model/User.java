package edu.northeastern.numad22fateam26.model;

public class User {
    private String userId;
    private String FCMToken;
    private String username;

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", FCMToken='" + FCMToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
