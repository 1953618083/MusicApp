package com.example.musicappdemo.entity;

public class LrcLine {
    public long timestamp;
    public String text;

    public LrcLine(long timestamp, String text) {
        this.timestamp = timestamp;
        this.text = text;
    }

    public LrcLine() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "LrcLine{" +
                "timestamp=" + timestamp +
                ", text='" + text + '\'' +
                '}';
    }
}