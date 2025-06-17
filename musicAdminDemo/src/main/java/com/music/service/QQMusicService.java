package com.music.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QQMusicService {

    private final RestTemplate qqMusicRestTemplate;

    // 搜索歌曲
    public List<SongDTO> searchSongs(String keyword, int page, int size) {
        String url = "/search/song?key={key}&page={page}&size={size}";

        Map<String, Object> params = new HashMap<>();
        params.put("key", keyword);
        params.put("page", page);
        params.put("size", size);

        QQResponse<SongSearchResult> QQresponse = qqMusicRestTemplate
                .getForObject(url, QQResponse.class, params);

        if (QQresponse.getCode() == 200) {
            return QQresponse.getData().getList();
        }
        throw new RuntimeException("QQ音乐搜索失败: " + QQresponse.getMessage());
    }

    // 获取歌曲播放URL
    public String getSongUrl(String songId) {
        String url = "/song/urls?id={id}";
        QQResponse<Map<String, String>> QQresponse = qqMusicRestTemplate
                .getForObject(url, QQResponse.class, Collections.singletonMap("id", songId));

        if (QQresponse.getCode() == 200) {
            return QQresponse.getData().values().iterator().next();
        }
        throw new RuntimeException("获取播放地址失败: " + QQresponse.getMessage());
    }

    // DTO定义
    @Data
    public static class QQResponse<T> {
        private int code;
        private String message;
        private T data;
    }

    @Data
    public static class SongSearchResult {
        private List<SongDTO> list;
        private int total;
    }

    @Data
    public static class SongDTO {
        private String id;
        private String name;
        private List<Artist> artists;
        private Album album;
        private int duration;
    }
    @Data
    public static class Artist {
        private String id;
        private String name;
        private String mid; // QQ音乐特有的mid字段
        private String picUrl; // 艺术家图片URL
    }

    @Data
    public static class Album {
        private String id;
        private String name;
        private String mid; // QQ音乐特有的mid字段
        private String picUrl; // 专辑封面URL
        private String publishTime; // 发行时间
    }
}