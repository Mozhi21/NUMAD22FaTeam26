package edu.northeastern.numad22fateam26.finalProject.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class ChatUserModel {

    private String id, lastMessage, lastMessageTo;
    private boolean unread;
    private List<String> uid;

    @ServerTimestamp
    private Date time;


    public ChatUserModel() {
    }

    public ChatUserModel(String id, String lastMessage, String lastMessageTo, boolean unread, List<String> uid, Date time) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.lastMessageTo = lastMessageTo;
        this.unread = unread;
        this.uid = uid;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getUid() {
        return uid;
    }

    public void setUid(List<String> uid) {
        this.uid = uid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLastMessageTo() {
        return lastMessageTo;
    }

    public void setLastMessageTo(String lastMessageTo) {
        this.lastMessageTo = lastMessageTo;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
