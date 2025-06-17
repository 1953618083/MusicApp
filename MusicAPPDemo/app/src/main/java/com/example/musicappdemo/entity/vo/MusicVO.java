package com.example.musicappdemo.entity.vo;

import java.io.Serializable;

public class MusicVO implements Serializable {

    private String id;
    private String musicName;
    private String musicUrl;
    private String picUrl;
    private String lrcUrl;
    private String author;
    private String info;
    private String year;
    private String createTime;
    private String updateTime;
    private Integer status;
    private String classifyName;

    public MusicVO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLrcUrl() {
        return lrcUrl;
    }

    public void setLrcUrl(String lrcUrl) {
        this.lrcUrl = lrcUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    @Override
    public String toString() {
        return "MusicVO{" +
                "id='" + id + '\'' +
                ", musicName='" + musicName + '\'' +
                ", musicUrl='" + musicUrl + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", lrcUrl='" + lrcUrl + '\'' +
                ", author='" + author + '\'' +
                ", info='" + info + '\'' +
                ", year='" + year + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", status='" + status + '\'' +
                ", classifyName='" + classifyName + '\'' +
                '}';
    }
}
