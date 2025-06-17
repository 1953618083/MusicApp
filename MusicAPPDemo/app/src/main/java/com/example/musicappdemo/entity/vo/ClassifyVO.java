package com.example.musicappdemo.entity.vo;

public class ClassifyVO {
    private String id;  // 修改为 String 类型
    private String name;

    // 修改构造函数以适应新的 id 类型
    public ClassifyVO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;  // 返回 String 类型的 id
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
