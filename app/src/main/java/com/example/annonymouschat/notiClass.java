package com.example.annonymouschat;

public class notiClass {

    String image,title,Body;

    public notiClass(String image, String title, String body) {
        this.image = image;
        this.title = title;
        Body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }
}
