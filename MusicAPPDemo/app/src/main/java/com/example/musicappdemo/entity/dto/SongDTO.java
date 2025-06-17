package com.example.musicappdemo.entity.dto;

import com.example.musicappdemo.entity.Album;
import com.example.musicappdemo.entity.Artist;

import java.io.Serializable;
import java.util.List;

// SongDTO.java
public class SongDTO implements Serializable {
    private String songid;
    private String songname;
    private List<Artist> singer;
    private Album album;
    private int duration;

    // Getters and Setters
    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public List<Artist> getSinger() {
        return singer;
    }

    public void setSinger(List<Artist> singer) {
        this.singer = singer;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}