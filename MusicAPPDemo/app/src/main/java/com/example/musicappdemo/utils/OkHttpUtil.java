package com.example.musicappdemo.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtil {

    private static final OkHttpClient client = new OkHttpClient();

    public interface Callback {
        void onSuccess(String result);
        void onFailure(Exception e);
    }

    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(new IOException("Unexpected code " + response));
                    return;
                }
                callback.onSuccess(response.body().string());
            }
        });
    }
}

