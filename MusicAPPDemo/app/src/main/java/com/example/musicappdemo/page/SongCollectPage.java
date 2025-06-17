package com.example.musicappdemo.page;

import static com.example.musicappdemo.page.SongPage.musicDB;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.musicappdemo.R;

import com.example.musicappdemo.adapter.MusicCollectAdapter;
import com.example.musicappdemo.entity.vo.ClassifyVO;
import com.example.musicappdemo.entity.vo.CollectMusicVO;
import com.example.musicappdemo.entity.vo.MusicClassifyVO;
import com.example.musicappdemo.entity.vo.MusicVO;
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


public class SongCollectPage extends Fragment {
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public List<CollectMusicVO> musicClassifyDB = new ArrayList<>();

    private View singerView;
    private GridView gv;
    private MusicCollectAdapter adapter;

    private Spinner categorySpinner;
    private List<ClassifyVO> classifyList = new ArrayList<>();
    private String currentClassifyId = "-1"; // -1表示全部
    private volatile boolean isLoading = false;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        singerView = inflater.inflate(R.layout.singer_page, null);
        gv = singerView.findViewById(R.id.singer_gv);
        categorySpinner = singerView.findViewById(R.id.category_spinner);

        initClassifyList();
        //1、获得数据源，也就是所有对象的列表

//        setListener();
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer positionCollect = null;
                CollectMusicVO musicVO = musicClassifyDB.get(position);
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
        return singerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoading) { // 添加条件判断
            initMusicClassifyList();
            isLoading = false;
        }
    }
    private void initClassifyList() {
        // 清空旧数据，避免重复添加
        classifyList.clear();
        classifyList.add(new ClassifyVO("-1", "全部"));  // 使用字符串类型的 ID

        new Thread(() -> {
            try {
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "");

                Request request = new Request.Builder()
                        .url(NetWorkAPi.classifyList)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();

                Response response = HTTP_CLIENT.newCall(request).execute();
                String json = response.body().string();
                System.out.println("classifyList json: " + json);
                JSONObject resultObject = new JSONObject(json);
                JSONArray data = resultObject.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject obj = data.getJSONObject(i);

                    String classifyId = obj.getString("classifyId"); // 修改为 String 类型的 id
                    String classifyName = obj.getString("classifyName"); // 确保返回的是正确的字段

                    classifyList.add(new ClassifyVO(classifyId, classifyName));
                }

                // 在 UI 线程中更新 Spinner
                getActivity().runOnUiThread(() -> {
                    ArrayAdapter<ClassifyVO> spinnerAdapter = new ArrayAdapter<>(
                            getContext(),
                            android.R.layout.simple_spinner_item,
                            classifyList
                    );
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(spinnerAdapter);

                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // 设置当前分类 ID
                            currentClassifyId = classifyList.get(position).getId();
                            initMusicClassifyList(); // 重新加载音乐列表
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "加载分类失败", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    private void initMusicClassifyList() {
        if (isLoading) {
            return; // 如果正在加载，直接返回
        }
        isLoading = true; // 标记为加载中

        musicClassifyDB.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url;
                    if (currentClassifyId.equals("-1")) {
                        // 获取全部收藏音乐
                        url = NetWorkAPi.initMusicCollectList + SharedPreferenceUtil.getString("account","");
                    } else {
                        // 获取指定分类的音乐
                        url = NetWorkAPi.musicByClassify  + currentClassifyId + "/music?userId=" + SharedPreferenceUtil.getString("userId", "");
                        Log.d("userId:", SharedPreferenceUtil.getString("userId", ""));
                    }

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "");

                    Request request = new Request.Builder()
                            .url(url)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    System.out.println("classifyList json: " + json);
                    JSONObject resultObject = new JSONObject(json);
                    JSONArray data = resultObject.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObjectMusic = data.getJSONObject(i);
                        String musicId = jsonObjectMusic.getString("musicId");
                        String musicName = jsonObjectMusic.getString("musicName");
                        String author = jsonObjectMusic.getString("author");
                        String picUrl = jsonObjectMusic.getString("picUrl");

                        CollectMusicVO collectMusicVO = new CollectMusicVO();
                        collectMusicVO.setMusicId(musicId);
                        collectMusicVO.setMusicName(musicName);
                        collectMusicVO.setAuthor(author);
                        collectMusicVO.setPicUrl(picUrl);

                        musicClassifyDB.add(collectMusicVO);
                    }

                    getActivity().runOnUiThread(() -> {
                        if (musicClassifyDB.isEmpty()) {
                            Toast.makeText(getContext(), "当前分类没有收藏歌曲", Toast.LENGTH_SHORT).show();
                        }
                        adapter = new MusicCollectAdapter(getContext(), musicClassifyDB);
                        gv.setAdapter(adapter);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show()
                    );
                }finally {
                    isLoading = false; // 无论成功与否，重置标志位
                }
                getActivity().runOnUiThread(() -> {
                    if (musicClassifyDB.isEmpty()) {
                        Toast.makeText(getContext(), "当前分类没有收藏歌曲", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged(); // 通知适配器刷新
                });
            }
        }).start();


    }

    public void setListener() {
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Mu classifyVO = musicClassifyDB.get(position);
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
//            }
//        });
    }
}
