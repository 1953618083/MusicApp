package com.example.musicappdemo.page;

import static com.example.musicappdemo.page.SongPage.musicDB;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicappdemo.R;
import com.example.musicappdemo.adapter.MusicCollectAdapter;
import com.example.musicappdemo.adapter.MusicLoveAdapter;
import com.example.musicappdemo.entity.vo.CollectMusicVO;
import com.example.musicappdemo.entity.vo.LoveMusicVO;
import com.example.musicappdemo.music.MusicActivity;
import com.example.musicappdemo.utils.NetWorkAPi;
import com.example.musicappdemo.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SongLoveFragment extends Fragment {

    private View view;
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public List<LoveMusicVO> loveMusicList = new ArrayList<>();

    private ListView listView;
    private MusicLoveAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song_love, container, false);
        initView(view);
        return view;
    }
    private void initView(View view) {
        listView = view.findViewById(R.id.song_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer positionCollect = null;
                LoveMusicVO musicVO = loveMusicList.get(position);
                String musicId = musicVO.getMusicId();
                for (int i = 0; i < musicDB.size(); i++) {
                    if (musicDB.get(i).getId().equals(musicId)) {
                        positionCollect = i;
                    }
                }
                if (positionCollect==null){
                    Toast.makeText(getContext(),"音乐库不存在该收藏音乐",Toast.LENGTH_LONG).show();
                    return;
                }
                //创建Intent对象，启动音乐播放界面
                Intent intent = new Intent(getContext(), MusicActivity.class);
                //将数据存入Intent对象，利用键值对
                intent.putExtra("name", musicVO.getMusicName());
                intent.putExtra("position", String.valueOf(positionCollect));
                //开启意图，进行跳转
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initMusicLoveList();
    }

    private void initMusicLoveList() {
        loveMusicList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "");

                    Request request = new Request.Builder()
                            .url(NetWorkAPi.initMusicLoveList + SharedPreferenceUtil.getString("account",""))
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject resultObject = new JSONObject(json);
                    JSONArray data = resultObject.getJSONArray("data");
                    if (data.length()>0){
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObjectMusic = data.getJSONObject(i);
                            String musicId = jsonObjectMusic.getString("musicId");
                            String musicName = jsonObjectMusic.getString("musicName");
                            String createTime = jsonObjectMusic.getString("createTime");
                            String author = jsonObjectMusic.getString("author");
                            String picUrl = jsonObjectMusic.getString("picUrl");


                            LoveMusicVO loveMusicVO = new LoveMusicVO();
                            loveMusicVO.setMusicId(musicId);
                            loveMusicVO.setMusicName(musicName);
                            loveMusicVO.setCreateTime(createTime);
                            loveMusicVO.setAuthor(author);
                            loveMusicVO.setPicUrl(picUrl);

                            loveMusicList.add(loveMusicVO);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new MusicLoveAdapter(getContext(), loveMusicList);
                                //3、为布局设置适配器
                                listView.setAdapter(adapter);
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"暂未喜欢歌曲",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}