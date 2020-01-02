package com.example.annonymouschat;

public class postClass {

    String text,time,receivedBy;

    public postClass(String text, String time, String receivedBy) {
        this.text = text;
        this.time = time;
        this.receivedBy = receivedBy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }
}
