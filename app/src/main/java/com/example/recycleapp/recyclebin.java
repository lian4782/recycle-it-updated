package com.example.recycleapp;

import java.util.ArrayList;

public class recyclebin {
    private int points;
    private int cnt;
    private ArrayList<recycleItem> binItemsList;

    public recyclebin() {
        this.points=0;
        this.cnt=0;
        this.binItemsList=new ArrayList<recycleItem>();
    }

    public void updateBin( recycleItem r) {
        this.points =this.points+r.getValue();
        this.cnt++;
        if (this.binItemsList!=null) {
            this.binItemsList.add(r);
        }
        else{
            ArrayList<recycleItem> temp=new ArrayList<recycleItem>();
            temp.add(r);
            this.binItemsList=temp;
        }
    }

    public int getCnt() {
        return cnt;
    }
    public int getPoints() {
        return points;
    }

    public ArrayList<recycleItem> getBinItemsList() {
        return binItemsList;
    }

    public void setBinItemsList(ArrayList<recycleItem> binItemsList) {
        this.binItemsList = binItemsList;
    }
}


