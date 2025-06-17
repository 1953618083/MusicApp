package com.example.musicappdemo.entity.search;

import java.util.List;

public class Response {
    private int code;
    private ResponseData data;
    private String keyword;
    private int priority;
    private List<Object> qc;
    private Semantic semantic;
    private SongData song;

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }

    // ... 其他字段的getter和setter

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Object> getQc() {
        return qc;
    }

    public void setQc(List<Object> qc) {
        this.qc = qc;
    }

    public Semantic getSemantic() {
        return semantic;
    }

    public void setSemantic(Semantic semantic) {
        this.semantic = semantic;
    }

    public SongData getSong() {
        return song;
    }

    public void setSong(SongData song) {
        this.song = song;
    }
}


