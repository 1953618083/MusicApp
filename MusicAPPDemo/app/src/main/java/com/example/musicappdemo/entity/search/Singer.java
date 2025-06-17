package com.example.musicappdemo.entity.search;

public class Singer {
    private int id;
    private String mid;
    private String name;
    private String name_hilight;

    // Getters and Setters
    // ...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_hilight() {
        return name_hilight;
    }

    public void setName_hilight(String name_hilight) {
        this.name_hilight = name_hilight;
    }
}
