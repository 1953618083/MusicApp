package com.example.musicappdemo.entity;

import java.io.Serializable;

// Artist.java
public class Artist implements Serializable {

    private String name;  // 对应 'name'

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}