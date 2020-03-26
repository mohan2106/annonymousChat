package com.example.annonymouschat;

public class universityClass {
    String name;
    String id;
    int member;

    public universityClass(String name, String id, int member) {
        this.name = name;
        this.id = id;
        this.member = member;
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

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }
}
