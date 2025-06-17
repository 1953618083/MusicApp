package com.example.musicappdemo.page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.musicappdemo.R;
import com.example.musicappdemo.adapter.MusicClassifyGridAdapter;
import com.example.musicappdemo.entity.vo.MusicClassifyVO;
import com.example.musicappdemo.entity.vo.MusicVO;
import com.example.musicappdemo.music.MusicActivity;
import com.example.musicappdemo.utils.NetWorkAPi;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SongPage extends Fragment {
    //声明视图变量view
    public static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    private GridView gv;

    private Banner banner;

    private MusicClassifyGridAdapter gridAdapter;
    private View view;
    private ListView listView;
    public List<MusicClassifyVO> musicClassifyDB = new ArrayList<>();
    public static List<MusicVO> musicDB = new ArrayList<>();
    public static List<String> musicNameList = new ArrayList<>();
    public static List<String> musicPicList = new ArrayList<>();
    public static List<String> musicUrlList = new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_list, null);
        //1、创建并绑定列表
        listView = view.findViewById(R.id.lv);
        banner = view.findViewById(R.id.banner);
        gv = view.findViewById(R.id.singer_gv);
        banner.setIndicator(new CircleIndicator(getContext()));//设置底部指示点
        banner.setIndicatorRadius(80);//设置指示点半径  像素
        //网络请求获取音乐
        initMusicList();
        //
        initMusicClassifyList();
        setListener();
        //设置列表条目监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicVO musicVO = musicDB.get(position);
                //创建Intent对象，启动音乐播放界面
                Intent intent = new Intent(SongPage.this.getContext(), MusicActivity.class);
                //将数据存入Intent对象，利用键值对
                intent.putExtra("name", musicVO.getMusicName());
                intent.putExtra("position", String.valueOf(position));
                //开启意图，进行跳转
                startActivity(intent);


            }
        });
        return view;
    }

    private void initMusicList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", null);
                    map.put("year", null);
                    map.put("status", null);

                    JSONObject jsonObject = new JSONObject(map);
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

                    Request request = new Request.Builder()
                            .url(NetWorkAPi.musicList)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject resultObject = new JSONObject(json);
                    JSONArray data = resultObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObjectMusic = data.getJSONObject(i);
                        String id = jsonObjectMusic.getString("id");
                        String musicName = jsonObjectMusic.getString("name");
                        String musicUrl = jsonObjectMusic.getString("url");
                        String picUrl = jsonObjectMusic.getString("picUrl");
                        String lrcUrl = jsonObjectMusic.getString("lrcUrl");
                        String author = jsonObjectMusic.getString("author");
                        String info = jsonObjectMusic.getString("info");
                        String year = jsonObjectMusic.getString("year");
                        String createTime = jsonObjectMusic.getString("createTime");
                        String updateTime = jsonObjectMusic.getString("updateTime");
                        String classifyName = jsonObjectMusic.getString("classifyName");
                        MusicVO musicVO = new MusicVO();
                        musicVO.setId(id);
                        musicVO.setMusicName(musicName);
                        musicVO.setMusicUrl(musicUrl);
                        musicVO.setPicUrl(picUrl);
                        musicVO.setLrcUrl(lrcUrl);
                        musicVO.setAuthor(author);
                        musicVO.setInfo(info);
                        musicVO.setYear(year);
                        musicVO.setCreateTime(createTime);
                        musicVO.setUpdateTime(updateTime);
                        musicVO.setClassifyName(classifyName);
                        musicDB.add(musicVO);
                    }
                    musicNameList = musicDB.stream().map(MusicVO::getMusicName).collect(Collectors.toList());
                    musicUrlList = musicDB.stream().map(MusicVO::getMusicUrl).collect(Collectors.toList());
                    musicPicList = musicDB.stream().map(MusicVO::getPicUrl).collect(Collectors.toList());
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                banner.setAdapter(new BannerImageAdapter<String>(musicPicList) {
                                    @Override
                                    public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                                        //进行网络图片加载
                                        Glide.with(holder.imageView)
                                                .load(data)
                                                .into(holder.imageView);
                                    }
                                });

                                //2、创建适配器对象
                                MyBaseAdapter adapter = new MyBaseAdapter();
                                //3、给列表设置适配器
                                listView.setAdapter(adapter);
                            }
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    public void setListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicClassifyVO classifyVO = musicClassifyDB.get(position);

                getClassifyMusic(classifyVO.getClassifyId());
//                Bundle bundle = new Bundle();
//                bundle.putString("classifyName", classifyVO.getClassifyName());
//
//                ClassifySongPage classifySongPage = new ClassifySongPage();
//                classifySongPage.setArguments(bundle);
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.content, classifySongPage, null)
//                        .addToBackStack(null)
//                        .commit();

            }
        });
    }

    private void getClassifyMusic(String classifyId) {
        musicDB.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "");

                    Request request = new Request.Builder()
                            .url(NetWorkAPi.getClassifyMusic + classifyId)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject resultObject = new JSONObject(json);
                    JSONArray data = resultObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObjectMusic = data.getJSONObject(i);
                        String id = jsonObjectMusic.getString("id");
                        String musicName = jsonObjectMusic.getString("name");
                        String musicUrl = jsonObjectMusic.getString("url");
                        String picUrl = jsonObjectMusic.getString("picUrl");
                        String lrcUrl = jsonObjectMusic.getString("lrcUrl");
                        String author = jsonObjectMusic.getString("author");
                        String info = jsonObjectMusic.getString("info");
                        String year = jsonObjectMusic.getString("year");
                        String createTime = jsonObjectMusic.getString("createTime");
                        String updateTime = jsonObjectMusic.getString("updateTime");
                        String classifyName = jsonObjectMusic.getString("classifyName");
                        MusicVO musicVO = new MusicVO();
                        musicVO.setId(id);
                        musicVO.setMusicName(musicName);
                        musicVO.setMusicUrl(musicUrl);
                        musicVO.setPicUrl(picUrl);
                        musicVO.setLrcUrl(lrcUrl);
                        musicVO.setAuthor(author);
                        musicVO.setInfo(info);
                        musicVO.setYear(year);
                        musicVO.setCreateTime(createTime);
                        musicVO.setUpdateTime(updateTime);
                        musicVO.setClassifyName(classifyName);
                        musicDB.add(musicVO);
                    }

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (musicDB.isEmpty()) {
                                    Toast.makeText(getContext(), "该分类下暂无歌曲", Toast.LENGTH_SHORT).show();
                                }
                                MyBaseAdapter adapter = new MyBaseAdapter();
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    private void initMusicClassifyList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "");

                    Request request = new Request.Builder()
                            .url(NetWorkAPi.initMusicClassifyList)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject resultObject = new JSONObject(json);
                    JSONArray data = resultObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObjectMusic = data.getJSONObject(i);
                        String classifyId = jsonObjectMusic.getString("classifyId");
                        String classifyName = jsonObjectMusic.getString("classifyName");
                        String createTime = jsonObjectMusic.getString("createTime");

                        MusicClassifyVO musicClassifyVO = new MusicClassifyVO();
                        musicClassifyVO.setClassifyId(classifyId);
                        musicClassifyVO.setClassifyName(classifyName);
                        musicClassifyVO.setCreateTime(createTime);

                        musicClassifyDB.add(musicClassifyVO);
                    }


                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gridAdapter = new MusicClassifyGridAdapter(requireContext(), musicClassifyDB);
                                gv.setAdapter(gridAdapter);
                            }
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return musicDB.size();
        }

        @Override
        public Object getItem(int i) {
            return musicDB.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            MusicVO musicVO = musicDB.get(i);
            //绑定视图，并且显示歌曲名和歌曲图片
            View view = View.inflate(SongPage.this.getContext(), R.layout.item_music, null);
            TextView songName = view.findViewById(R.id.song_name);
            TextView songAuthor = view.findViewById(R.id.song_author);
            TextView songClassify = view.findViewById(R.id.song_classify);
            TextView songYear = view.findViewById(R.id.song_year);
            ImageView songPic = view.findViewById(R.id.song_pic);
            songName.setText("歌名：" + musicVO.getMusicName());
            songAuthor.setText("作者：" + musicVO.getAuthor());
            songClassify.setText("标签：" + musicVO.getClassifyName());
            songYear.setText("年份：" + musicVO.getYear());

            Glide.with(getContext())
                    .asBitmap()
                    .load(musicVO.getPicUrl())
                    .centerCrop()
                    .into(new BitmapImageViewTarget(songPic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.
                                            create(getView().getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(30);
                            songPic.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            return view;
        }
    }


}
