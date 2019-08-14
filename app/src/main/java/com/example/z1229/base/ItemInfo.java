package com.example.z1229.base;

import java.io.Serializable;

public class ItemInfo implements Serializable {

    private int photo;
    private int cal;
    private int ke;
    private String name;

    public ItemInfo(int photo, int cal, String name,int ke){
        this.photo = photo;
        this.cal = cal;
        this.ke = ke;
        this.name = name;
    }

    public int getCal() {
        return cal;
    }

    public String getName() {
        return name;
    }

    public int getPhoto() {
        return photo;
    }

    public int getKe() {
        return ke;
    }

    public void setKe(int ke) {
        this.ke = ke;
    }

    public void setName(String name) {
        this.name = name;
    }
}
