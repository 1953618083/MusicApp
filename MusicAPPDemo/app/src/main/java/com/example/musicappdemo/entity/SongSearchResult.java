package com.example.musicappdemo.entity;

import com.example.musicappdemo.entity.dto.SongDTO;
import com.example.musicappdemo.entity.search.Song;

import java.io.Serializable;
import java.util.List;

public class SongSearchResult {
    private Song song;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
    public class Song implements Serializable {
        private int curnum;
        private int curpage;
        private List<SongDTO> list; // 这里是歌曲列表

        public List<SongDTO> getList() {
            return list;
        }

        public void setList(List<SongDTO> list) {
            this.list = list;
        }

        public int getCurnum() {
            return curnum;
        }

        public void setCurnum(int curnum) {
            this.curnum = curnum;
        }

        public int getCurpage() {
            return curpage;
        }

        public void setCurpage(int curpage) {
            this.curpage = curpage;
        }
    }
}

