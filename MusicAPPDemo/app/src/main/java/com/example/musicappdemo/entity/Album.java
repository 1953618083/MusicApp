package com.example.musicappdemo.entity;

import java.io.Serializable;

// Album.java
public class Album implements Serializable {
    private String albumid;  // 对应搜索结果中的 'albumid'
    private String albummid;  // 对应搜索结果中的 'albummid'
    private String albumname;  // 对应搜索结果中的 'albumname'

    // Getters and Setters
    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getAlbummid() {
        return albummid;
    }

    public void setAlbummid(String albummid) {
        this.albummid = albummid;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }
}