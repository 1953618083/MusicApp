package com.example.musicappdemo.entity;

// QQResponse.java
public class QQResponse<T> {
    private int code;
    //private String message;
    private T data;

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /*public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }*/

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}