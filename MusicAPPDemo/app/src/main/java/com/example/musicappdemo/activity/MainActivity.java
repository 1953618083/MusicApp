package com.example.musicappdemo.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicappdemo.R;
import com.example.musicappdemo.adapter.MusicClassifyGridAdapter;
import com.example.musicappdemo.entity.Album;
import com.example.musicappdemo.entity.Artist;
import com.example.musicappdemo.entity.QQResponse;
import com.example.musicappdemo.entity.SongSearchResult;
import com.example.musicappdemo.entity.dto.SongDTO;
import com.example.musicappdemo.entity.vo.MusicClassifyVO;
import com.example.musicappdemo.entity.vo.MusicVO;
import com.example.musicappdemo.music.QQMusicApiClient;
import com.example.musicappdemo.music.QQMusicApiService;
//import com.example.musicappdemo.page.HistoryPage;
import com.example.musicappdemo.page.AppInfoFragment;
import com.example.musicappdemo.page.RecommendPage;
import com.example.musicappdemo.page.SongCollectPage;
import com.example.musicappdemo.page.SongPage;
import com.example.musicappdemo.page.UserPage;
import com.example.musicappdemo.utils.NetWorkAPi;
import com.example.musicappdemo.utils.SharedPreferenceUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView menu1, menu2, menu3,menu4;
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    public List<MusicClassifyVO> musicClassifyDB = new ArrayList<>();

    private FragmentManager fm;
    private FragmentTransaction ft;
    private Banner banner;

    private GridView gv;
    private MusicClassifyGridAdapter gridAdapter;

    public List<MusicVO> musicDB = new ArrayList<>();
    public List<String> musicPicList = new ArrayList<>();

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private QQMusicApiService apiService;

    private ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferenceUtil.init(this, "user_preference");
//        String account = SharedPreferenceUtil.getString("account", "");
//        Toast.makeText(getApplicationContext(), account + "用户：欢迎您", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMusic(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //必须重写的onCreate方法
        super.onCreate(savedInstanceState);
        //设定对应的布局文件
        setContentView(R.layout.activity_main);
        // 初始化工具栏和侧边栏
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        apiService = QQMusicApiClient.getInstance();
        //去除标题栏
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }*/



        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // 设置汉堡菜单按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 初始化搜索框
        /*searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 处理搜索提交
                searchMusic(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 实时搜索建议
                return false;
            }
        });*/

        // 侧边栏菜单项点击事件
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // 每次点击都创建新的FragmentTransaction
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (id == R.id.nav_home) {
                ft.replace(R.id.content, new SongPage());
            }
            else if (id == R.id.nav_favorites) {
                ft.replace(R.id.content, new SongCollectPage());
            }else if (id == R.id.nav_about) {
                ft.replace(R.id.content, new AppInfoFragment());
            }
            /* else if (id == R.id.nav_history) {
                //ft.replace(R.id.content, new HistoryPage());
            } else if (id == R.id.nav_settings) {
               //ft.replace(R.id.content, new SettingsFragment());
            }*/


            // 添加过渡动画
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            // 提交事务
            ft.commit();

            // 关闭抽屉
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        //绑定控件
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);
        menu4 = findViewById(R.id.menu4);
        //设置监听器
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        //获得布局管理器
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        //默认情况下显示frag1（音乐列表界面）
        ft.replace(R.id.content, new SongPage());
        ft.commit();
    }

    private void searchMusic(String keyword) {
        // 显示加载进度
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("搜索中...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {
            try {

                URL url = new URL("http://8.138.114.23:3200/getSearchByKey?key=" + URLEncoder.encode(keyword, "UTF-8") + "&page=1&size=20");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    reader.close();

                    String responseText = responseBuilder.toString();
                    Log.d("HTTP", "Response: " + responseText);

                    JSONObject rootObj = new JSONObject(responseText);
                    JSONObject responseObj = rootObj.getJSONObject("response");

                    int code = responseObj.getInt("code");
                    if (code == 0) {
                        JSONObject dataObj = responseObj.getJSONObject("data");
                        JSONObject songObj = dataObj.getJSONObject("song");
                        JSONArray listArray = songObj.getJSONArray("list");

                        ArrayList<SongDTO> songs = new ArrayList<>();

                        for (int i = 0; i < listArray.length(); i++) {
                            JSONObject songItem = listArray.getJSONObject(i);

                            SongDTO dto = new SongDTO();
                            dto.setSongid(String.valueOf(songItem.optInt("songid")));
                            dto.setSongname(songItem.optString("songname"));
                            dto.setDuration(songItem.optInt("interval"));

                            // 设置 singer
                            JSONArray singerArray = songItem.optJSONArray("singer");
                            List<Artist> singers = new ArrayList<>();
                            if (singerArray != null) {
                                for (int j = 0; j < singerArray.length(); j++) {
                                    JSONObject singerObj = singerArray.getJSONObject(j);
                                    Artist artist = new Artist();
                                    //artist.setId(singerObj.optInt("id"));
                                    //artist.setMid(singerObj.optString("mid"));
                                    artist.setName(singerObj.optString("name"));
                                    singers.add(artist);
                                }
                            }
                            dto.setSinger(singers);

                            // 设置 album
                            Album album = new Album();
                            //album.setAlbumid(songItem.optInt("albumid"));
                            album.setAlbummid(songItem.optString("albummid"));
                            album.setAlbumname(songItem.optString("albumname"));
                            dto.setAlbum(album);

                            songs.add(dto);
                        }

                        runOnUiThread(() -> {
                            if (songs.isEmpty()) {
                                Toast.makeText(MainActivity.this, "没有找到相关歌曲", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();  // 在跳转前关闭进度框
                                navigateToSearchResult(keyword, songs);
                            }
                        });

                    } else {
                        runOnUiThread(() -> showError("服务器返回失败 code: " + code));
                    }

                } else {
                    runOnUiThread(() -> showError("响应码异常: " + responseCode));
                }

                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> showError("请求出错: " + e.getMessage()));
            }
        }).start();

        /*Call<QQResponse<SongSearchResult>> call = apiService.searchSongs(keyword, 1, 20);
        call.enqueue(new Callback<QQResponse<SongSearchResult>>() {
            @Override
            public void onResponse(Call<QQResponse<SongSearchResult>> call,retrofit2.Response<QQResponse<SongSearchResult>> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {

                    Log.d("SearchResult", "Response Data: " + new Gson().toJson(response.body()));
                    QQResponse<SongSearchResult> qqResponse = response.body();
                    Log.d("SearchResponse", "Response Code: " + qqResponse.getCode());
                    SongSearchResult result = qqResponse.getData();
                    Log.d("SearchResult", "Data object: " + result);
                    if (qqResponse.getCode() == 0) {
                        SongSearchResult searchResult = qqResponse.getData();
                        Log.d("SearchResult", "Code: " + qqResponse.getCode());
                        //Log.d("SearchResult", "Message: " + qqResponse.getMessage());
                        Log.d("SearchResult", "Data: " + qqResponse.getData());
                        Log.d("SearchResponse", "SearchResult: " + (searchResult != null ? "Not null" : "Null"));
                        Log.d("SearchResult", "Received data: " + qqResponse.getData());
                        if (searchResult != null && searchResult.getSong() != null) {
                            List<SongDTO> songs = searchResult.getSong().getList();  // 获取歌曲列表
                            Log.d("SearchResponse", "Songs List Size: " + (songs != null ? songs.size() : 0));

                            if (songs != null && !songs.isEmpty()) {
                                navigateToSearchResult(keyword, songs);
                            } else {
                                Log.e("SearchResponse", "No songs found in the search result.");
                                showError("搜索结果为空");
                            }
                        } else {
                            Log.e("SearchResponse", "SearchResult or Song is null.");
                            showError("搜索结果为空");
                        }
                    } else {
                        //Log.e("SearchResponse", "Error: " + qqResponse.getMessage());
                        showError("Error: ");
                    }
                } else {
                    Log.e("SearchResponse", "Request failed or body is null.");
                    showError("请求失败");
                }
            }

            @Override
            public void onFailure(Call<QQResponse<SongSearchResult>> call, Throwable t) {
                progressDialog.dismiss();
                showError(t.getMessage());
            }
        });*/
    }

    private void navigateToSearchResult(String keyword, List<SongDTO> songs) {
        if (songs != null) {
            Log.d("SearchResultActivity", "收到歌曲数量：" + songs.size());
        } else {
            Log.e("SearchResultActivity", "歌曲数据为 null");
        }
        if (songs.isEmpty()) {
            Toast.makeText(this, "没有找到相关歌曲", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("search_keyword", keyword);
        intent.putExtra("search_results", new ArrayList<>(songs)); // List<SongDTO> 要可序列化
        startActivity(intent);
    }

    private void showError(String message) {
        runOnUiThread(() ->
                Toast.makeText(MainActivity.this, "搜索出错: " + message, Toast.LENGTH_SHORT).show()
        );
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
                    musicPicList = musicDB.stream().map(MusicVO::getPicUrl).collect(Collectors.toList());

                    runOnUiThread(new Runnable() {
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
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    //点击事件
    public void onClick(View v) {
        ft = fm.beginTransaction();
        //根据控件id来切换页面
        if (v.getId() == R.id.menu1) {
            ft.replace(R.id.content, new SongPage());
        } else if (v.getId() == R.id.menu2) {
            ft.replace(R.id.content, new SongCollectPage());
        }else if (v.getId() == R.id.menu3) {
            ft.replace(R.id.content, new UserPage());
        }else if(v.getId()==R.id.menu4){
            ft.replace(R.id.content, new RecommendPage());
        }
        ft.commit();
    }
}
