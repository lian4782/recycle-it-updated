package com.example.recycleapp;

public class UserForRating {
    private String name;
    private String email;
    private recyclebin bin;
    private String picName;
    public UserForRating(String name, String email, recyclebin bin, String picName) {
        this.name = name;
        this.email = email;
        this.bin = bin;
        this.picName=picName;
    }

    public String getPicName() {
        return picName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public recyclebin getBin() {
        return bin;
    }

    public void setBin(recyclebin bin) {
        this.bin = bin;
    }
}
