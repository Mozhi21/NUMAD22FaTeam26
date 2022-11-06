package edu.northeastern.numad22fateam26.model;

public class Sticker {
    String id;
    String count;

    public Sticker(String id, String count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void increaseCount() {
        this.count = String.valueOf(Integer.parseInt(this.count) + 1);
    }

}

