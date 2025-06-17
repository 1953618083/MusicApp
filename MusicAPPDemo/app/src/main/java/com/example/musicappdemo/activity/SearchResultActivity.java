package com.example.musicappdemo.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicappdemo.R;
import com.example.musicappdemo.adapter.SearchResultAdapter;
import com.example.musicappdemo.entity.SongSearchResult;
import com.example.musicappdemo.entity.dto.SongDTO;
import com.example.musicappdemo.listener.OnSongItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity implements OnSongItemClickListener {

    private Toolbar toolbar;
    private TextView tvSearchKeyword;
    private RecyclerView rvSearchResults;
    private SearchResultAdapter adapter;
    private List<SongDTO> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        initViews();
        setupToolbar();
        loadSearchResults();
        setupRecyclerView();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvSearchKeyword = findViewById(R.id.tv_search_keyword);
        rvSearchResults = findViewById(R.id.rv_search_results);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadSearchResults() {
        String keyword = getIntent().getStringExtra("search_keyword");
        @SuppressWarnings("unchecked")
        ArrayList<SongDTO> receivedSongs = (ArrayList<SongDTO>) getIntent().getSerializableExtra("search_results");

        if (keyword != null) {
            tvSearchKeyword.setText(String.format("搜索结果: \"%s\"", keyword));
        }

        if (receivedSongs == null || receivedSongs.isEmpty()) {
            Toast.makeText(this, "没有找到相关歌曲", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        searchResults = receivedSongs;
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));

        // 检查适配器是否为空并更新数据
        if (adapter == null) {
            adapter = new SearchResultAdapter(this, searchResults, this);
            rvSearchResults.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged(); // 如果数据已经加载过，刷新适配器
        }
    }

    @Override
    public void onSongItemClick(SongDTO song, int position) {
        // 处理歌曲点击事件，例如播放歌曲
        playSong(song);
    }

    @Override
    public void onSongMenuClick(SongDTO song, int position) {
        // 处理菜单点击事件，例如添加到播放列表
        addToPlaylist(song);
    }

    private void playSong(SongDTO song) {
        // 实现播放逻辑

    }

    private void addToPlaylist(SongDTO song) {
        // 实现添加到播放列表逻辑

    }
}