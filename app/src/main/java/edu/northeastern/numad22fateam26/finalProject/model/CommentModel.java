package edu.northeastern.numad22fateam26.finalProject.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class CommentModel {

    String comment, commentID, postID, uid, name, profileImageUrl;

    @ServerTimestamp
    private Date timestamp;

    public CommentModel() {
    }

    public CommentModel(String comment, String commentID, String postID, String uid, String name, String profileImageUrl, Date timestamp) {
        this.comment = comment;
        this.commentID = commentID;
        this.postID = postID;
        this.uid = uid;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}