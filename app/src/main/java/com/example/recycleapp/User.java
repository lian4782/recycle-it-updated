package com.example.recycleapp;

import java.util.ArrayList;

public class User {
    private String key;
    private String name;
    private String email;
    private String password;
    private recyclebin BlueBin;
    private recyclebin OrangeBin;
    private recyclebin PurpleBin;
    private recyclebin Allbin;
    private String picName;

    public String getKey() {
        return key;
    }

    public User() {
        this.key=null;
        this.name = null;
        this.email = null;
        this.password = null;
        this.BlueBin=null;
        this.OrangeBin=null;
        this.PurpleBin=null;
        this.Allbin=null;
        this.picName=null;

    }


    public User(String key , String name, String email, String password, String picName) {
        this.key=key;
        this.name = name;
        this.email = email;
        this.password = password;
        this.BlueBin=new recyclebin();
        this.OrangeBin=new recyclebin();
        this.PurpleBin=new recyclebin();
        this.Allbin=new recyclebin();
        this.picName=picName;
    }

    public User(String key , String name, String email, String password, recyclebin BlueBin, recyclebin OrangeBin, recyclebin PurpleBin, recyclebin Allbin, String picName) {
        this.key=key;
        this.name = name;
        this.email = email;
        this.password = password;
        this.BlueBin=BlueBin;
        this.OrangeBin=OrangeBin;
        this.PurpleBin=PurpleBin;
        this.Allbin=Allbin;
        this.picName=picName;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getPicName() {
        return picName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public recyclebin getAllbin() {
        return Allbin;
    }

    public recyclebin getBlueBin() {
        return BlueBin;
    }

    public recyclebin getOrangeBin() {
        return OrangeBin;
    }

    public recyclebin getPurpleBin() {
        return PurpleBin;
    }


    public void setBlueBin(recyclebin blueBin) {
        BlueBin = blueBin;
    }

    public void setOrangeBin(recyclebin orangeBin) {
        OrangeBin = orangeBin;
    }

    public void setPurpleBin(recyclebin purpleBin) {
        PurpleBin = purpleBin;
    }

    public void setAllbin(recyclebin allbin) {
        Allbin = allbin;
    }
}


