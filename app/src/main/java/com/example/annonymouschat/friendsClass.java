package com.example.annonymouschat;

public class friendsClass {
    String key;
    String date;
    String name;
    Integer img;
    boolean status;

    public friendsClass(String key, String date) {
        this.key = key;
        this.date = date;
        this.name = "";
        this.img = R.drawable.c1;
        this.status = false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
