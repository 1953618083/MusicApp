package com.example.musicappdemo.music;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// QQMusicApiClient.java
public class QQMusicApiClient {
    private static final String BASE_URL = "http://10.0.2.2:3200/";
    private static QQMusicApiService instance;

    public static QQMusicApiService getInstance() {
        if (instance == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(QQMusicApiService.class);
        }
        return instance;
    }
}
