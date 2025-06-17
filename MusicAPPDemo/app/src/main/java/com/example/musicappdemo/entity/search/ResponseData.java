package com.example.musicappdemo.entity.search;

import java.util.List;

public class ResponseData<T> {
    private int code;
    private String msg;
    private T data;

    // Getter & Setter

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
