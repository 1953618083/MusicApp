package com.example.musicappdemo.entity.search;

import java.util.List;

public class SongData {
    private int curnum;
    private int curpage;
    private List<Song> list;

    // Getters and Setters
    // ...

    public int getCurnum() {
        return curnum;
    }

    public void setCurnum(int curnum) {
        this.curnum = curnum;
    }

    public int getCurpage() {
        return curpage;
    }

    public void setCurpage(int curpage) {
        this.curpage = curpage;
    }

    public List<Song> getList() {
        return list;
    }

    public void setList(List<Song> list) {
        this.list = list;
    }
}