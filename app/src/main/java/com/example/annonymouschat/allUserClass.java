package com.example.annonymouschat;

public class allUserClass {
    String name,image,id,status,online;

    public allUserClass(String name, String image, String id, String status, String online) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.status = status;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
