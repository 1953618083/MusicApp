package com.example.musicappdemo.entity.search;

import java.util.List;

public class Semantic {
    private int curnum;
    private int curpage;
    private List<Object> list;
    private int totalnum;

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

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public int getTotalnum() {
        return totalnum;
    }

    public void setTotalnum(int totalnum) {
        this.totalnum = totalnum;
    }
}
