package com.example.musicappdemo.entity.search;

import com.example.musicappdemo.entity.Singer;

import java.util.List;

public class Song {
    private int albumid;
    private String albummid;
    private String albumname;
    private String albumname_hilight;
    private int alertid;
    private int belongCD;
    private int cdIdx;
    private int chinesesinger;
    private String docid;
    private List<Grp> grp;
    private int interval;
    private int isonly;
    private String lyric;
    private String lyric_hilight;
    private String media_mid;
    private int msgid;
    private int newStatus;
    private long nt;
    private Pay pay;
    private Preview preview;
    private long pubtime;
    private int pure;
    private List<Singer> singer;
    private int size128;
    private int size320;
    private int sizeape;
    private long sizeflac;
    private int sizeogg;
    private int songid;
    private String songmid;
    private String songname;
    private String songname_hilight;
    private String strMediaMid;
    private int stream;
    private int switch_;
    private int t;
    private int tag;
    private int type;
    private int ver;
    private String vid;

    // Getters and Setters
    // 注意：switch是Java关键字，所以使用switch_
    public int getSwitch() {
        return switch_;
    }

    public void setSwitch(int switch_) {
        this.switch_ = switch_;
    }
    // ... 其他字段的getter和setter
}

