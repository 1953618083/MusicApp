package com.example.musicappdemo.page;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicappdemo.R;
import com.example.musicappdemo.adapter.RecommendAdapter;
import com.example.musicappdemo.entity.vo.MusicVO;
import com.example.musicappdemo.music.MusicActivity;
import com.example.musicappdemo.utils.NetWorkAPi;
import com.example.musicappdemo.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.musicappdemo.page.SongPage.musicDB;
import static com.example.musicappdemo.page.SongPage.musicPicList;

public class RecommendPage extends Fragment {

    private RecyclerView recyclerView;
    private RecommendAdapter adapter;
    private List<MusicVO> recommendList = new ArrayList<>();
    //public static List<MusicVO> musicDB = new ArrayList<>();
    public RecommendPage() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommend, container, false);

        recyclerView = view.findViewById(R.id.recommend_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecommendAdapter(getContext(), recommendList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(musicVO -> {
            // 在全局 musicDB 中查找当前 musicVO 的位置
            int position = findMusicPositionInGlobalDB(musicVO);
            if (position == -1) {
                Toast.makeText(getContext(), "歌曲未找到", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getContext(), MusicActivity.class);
            intent.putExtra("position", String.valueOf(position)); // 传递正确的位置
            startActivity(intent);
        });

        loadRecommendData();

        return view;
    }
    //在全局 musicDB 中查找 MusicVO 的位置
    private int findMusicPositionInGlobalDB(MusicVO target) {
        for (int i = 0; i < musicDB.size(); i++) {
            MusicVO item = musicDB.get(i);
            if (item.getId().equals(target.getId())) { // 使用唯一标识符（如ID）匹配
                return i;
            }
        }
        return -1;
    }
    private void loadRecommendData() {
        new Thread(() -> {
            try {
                URL url = new URL(NetWorkAPi.MusicRecommend+ "?userId=" + SharedPreferenceUtil.getString("userId", ""));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    connection.disconnect();

                    // 解析 JSON 并更新 UI
                    parseJsonAndUpdate(response.toString());
                } else {
                    showToast("服务器响应失败：" + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("请求异常：" + e.getMessage());
            }
        }).start();
    }
    private void parseJsonAndUpdate(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("data"); // ✔ 从对象中获取 "data" 字段

            List<MusicVO> tempList = new ArrayList<>();
            musicDB.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                MusicVO music = new MusicVO();
                music.setId(obj.optString("id"));
                music.setMusicName(obj.optString("name")); // ✔ JSON 字段叫 name，不是 musicName
                music.setAuthor(obj.optString("author"));
                music.setPicUrl(obj.optString("picUrl"));
                music.setMusicUrl(obj.optString("url")); // ✔ JSON 字段叫 url，不是 musicUrl
                music.setLrcUrl(obj.optString("lrcUrl"));
                music.setInfo(obj.optString("info"));
                music.setYear(obj.optString("year"));
                music.setCreateTime(obj.optString("createTime"));
                music.setUpdateTime(obj.optString("updateTime"));
                music.setStatus(obj.optInt("status"));
                music.setClassifyName(obj.optString("classifyName"));
                Log.d("RecommendAdapter", "分类为: " + music.getClassifyName());
                tempList.add(music);
                musicDB.add(music);
            }
            musicPicList = musicDB.stream().map(MusicVO::getPicUrl).collect(Collectors.toList());

            requireActivity().runOnUiThread(() -> {
                recommendList.clear();
                recommendList.addAll(tempList);
                adapter.notifyDataSetChanged();
            });

        } catch (JSONException e) {
            e.printStackTrace();
            showToast("JSON 解析出错：" + e.getMessage());
        }
    }
    private void showToast(String msg) {
        requireActivity().runOnUiThread(() ->
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show()
        );
    }
}
