package com.example.annonymouschat;

public class YourThredClass {
    String name,id,date,text,profile_image,post_image,post_id,nLike,ncomment;
    double credits;
    boolean liked;

    public YourThredClass(String name, String id, String date, String text, String profile_image, String post_image, String post_id, String nLike, String ncomment, double credits,boolean liked) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.text = text;
        this.profile_image = profile_image;
        this.post_image = post_image;
        this.post_id = post_id;
        this.nLike = nLike;
        this.ncomment = ncomment;
        this.credits = credits;
        this.liked = false;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getPost_image() {
        return post_image;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getnLike() {
        return nLike;
    }

    public String getNcomment() {
        return ncomment;
    }

    public double getCredits() {
        return credits;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setnLike(String nLike) {
        this.nLike = nLike;
    }

    public void setNcomment(String ncomment) {
        this.ncomment = ncomment;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }
}
