package com.example.musicappdemo.listener;

import com.example.musicappdemo.entity.dto.SongDTO;

public interface OnSongItemClickListener {
    void onSongItemClick(SongDTO song, int position);
    void onSongMenuClick(SongDTO song, int position);
}
