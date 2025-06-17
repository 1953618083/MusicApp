package com.example.musicappdemo.entity.vo;

public class MusicClassifyVO {
    private String classifyId;

    private String classifyName;

    private String createTime;

    private Integer musicCount;

    public MusicClassifyVO() {
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(Integer musicCount) {
        this.musicCount = musicCount;
    }

    @Override
    public String toString() {
        return "MusicClassifyVO{" +
                "classifyId='" + classifyId + '\'' +
                ", classifyName='" + classifyName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", musicCount=" + musicCount +
                '}';
    }
}
