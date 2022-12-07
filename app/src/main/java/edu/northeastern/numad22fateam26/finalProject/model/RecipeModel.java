package edu.northeastern.numad22fateam26.finalProject.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class RecipeModel {

    private String id, uid, recipe;

    @ServerTimestamp
    private Date timestamp;

    public RecipeModel() {
    }

    public RecipeModel( String id, String uid, Date timestamp, String recipe) {
        this.id = id;
        this.timestamp = timestamp;
        this.uid = uid;
        this.recipe = recipe;
    }
    public String getRecipe() { return recipe; }

    public void setRecipe(String recipe) {
        this.recipe= recipe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
