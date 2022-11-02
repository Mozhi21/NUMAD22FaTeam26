package edu.northeastern.numad22fateam26.model;

public class User {
    String realname, password;

    public User() {
    }
    
    public User(String username) {
        this.password = password;
    }

    public User(String realname, String username) {
        this.realname = realname;
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUsername() {
        return password;
    }

    public void setUsername(String username) {
        this.password = username;
    }
}
