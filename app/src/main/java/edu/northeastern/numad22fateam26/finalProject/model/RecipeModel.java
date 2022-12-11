package edu.northeastern.numad22fateam26.finalProject.model;

import java.util.List;

public class RecipeModel {

    private String postId;
    private String uid;
    private List<String> steps;
    private List<String> ingredients;

    public RecipeModel() {}

    public RecipeModel(String postId, String uid, List<String> steps, List<String> ingredients) {
        this.postId = postId;
        this.uid = uid;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }


    public List<String> getIngredients() { return ingredients; }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}
