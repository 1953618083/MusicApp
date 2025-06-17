package com.example.musicappdemo.entity.vo;

public class LoveMusicVO {

    private String musicId;
    private String musicName;
    private String author;
    private String createTime;
    private String picUrl;

    public LoveMusicVO() {
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CollectMusicVO{" +
                "musicId='" + musicId + '\'' +
                ", musicName='" + musicName + '\'' +
                ", author='" + author + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
