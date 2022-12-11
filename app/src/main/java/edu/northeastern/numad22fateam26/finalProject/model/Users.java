package edu.northeastern.numad22fateam26.finalProject.model;

public class Users {

    private String email, name, profileImage, uid, status, search;
    private boolean online;

    public Users() {
    }

    public Users(String email, String name, String profileImage, String uid, String status, String search,
                 boolean online) {
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.uid = uid;
        this.status = status;
        this.search = search;
        this.online = online;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}