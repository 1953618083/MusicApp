package com.example.musicappdemo.music;

import com.example.musicappdemo.entity.QQResponse;
import com.example.musicappdemo.entity.SongSearchResult;
import com.example.musicappdemo.entity.dto.SongDTO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

// QQMusicApiService.java
public interface QQMusicApiService {

    @GET("getSearchByKey")
    Call<QQResponse<SongSearchResult>> searchSongs(
            @Query("key") String keyword,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("song/urls")
    Call<QQResponse<Map<String, String>>> getSongUrls(
            @Query("id") String songId
    );

    @GET("recommend/songs")
    Call<QQResponse<List<SongDTO>>> getDailyRecommendations(
            @Header("Cookie") String cookie
    );
}