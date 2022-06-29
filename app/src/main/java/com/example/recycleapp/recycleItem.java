package com.example.recycleapp;

public class recycleItem {
    private String name;
    private int value;
    private String picname;
    private String kindOfBin;
    private String date;

    public recycleItem(String name, int value, String kindOfBin, String picname) {
        this.name = name;
        this.value = value;
        this.kindOfBin = kindOfBin;
        this.picname = picname;
    }

    public recycleItem(String name, int value, String kindOfBin, String picname, String date) {
        this.name = name;
        this.value = value;
        this.kindOfBin = kindOfBin;
        this.picname = picname;
        this.date=date;
    }

    public recycleItem() {
        this.name = null;
        this.value = 0;
        this.kindOfBin = null;
        this.picname = null;
        this.date=null;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public String getKindOfBin() {
        return kindOfBin;
    }

    public void setKindOfBin(String kindOfBin) {
        this.kindOfBin = kindOfBin;
    }
}