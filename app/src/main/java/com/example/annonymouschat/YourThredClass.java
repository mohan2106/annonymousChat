package com.example.annonymouschat;

public class YourThredClass {
    String name,id,date,text,profile_image,post_image,post_id,nLike,nDislike,ncomment;

    public YourThredClass(String name, String id, String date, String text, String profile_image, String post_image, String post_id, String nLike, String nDislike, String ncomment) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.text = text;
        this.profile_image = profile_image;
        this.post_image = post_image;
        this.post_id = post_id;
        this.nLike = nLike;
        this.nDislike = nDislike;
        this.ncomment = ncomment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getnLike() {
        return nLike;
    }

    public void setnLike(String nLike) {
        this.nLike = nLike;
    }

    public String getnDislike() {
        return nDislike;
    }

    public void setnDislike(String nDislike) {
        this.nDislike = nDislike;
    }

    public String getNcomment() {
        return ncomment;
    }

    public void setNcomment(String ncomment) {
        this.ncomment = ncomment;
    }
}
