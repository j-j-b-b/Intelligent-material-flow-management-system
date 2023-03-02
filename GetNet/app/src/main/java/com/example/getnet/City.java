package com.example.getnet;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.ParcelableSpan;
import android.util.Log;

public class City  {
    private int id;
    private String name;
    private String num;
    private int x;
    private int y;
    public City(){

    }
    public City(int id,String name,String num,int x,int y){
        this.id=id;
        this.name=name;
        this.num=num;
        this.x=x;
        this.y=y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
