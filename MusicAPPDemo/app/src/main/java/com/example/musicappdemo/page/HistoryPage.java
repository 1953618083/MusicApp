/*
package com.example.musicappdemo.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicappdemo.R;
import com.example.musicappdemo.adapter.HistoryAdapter;
import com.example.musicappdemo.entity.vo.PageVO;
import com.example.musicappdemo.entity.vo.PlayHistoryVO;
import com.example.musicappdemo.utils.NetWork;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryPage extends Fragment {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private HistoryAdapter adapter;

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    private int pageNum = 1;
    private final int pageSize = 20;
    private boolean isLastPage = false;

    // 假设当前登录用户ID，实际根据登录态动态获取
    private Long currentUserId = 123L;

    private List<PlayHistoryVO> historyList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_play_history, container, false);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.historyRecyclerView);
        emptyView = view.findViewById(R.id.emptyView);

        adapter = new HistoryAdapter(getContext(), historyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        initListeners();
        fetchHistory(true);

        return view;
    }

    private void initListeners() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                isLastPage = false;
                fetchHistory(true);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (!isLastPage) {
                    pageNum++;
                    fetchHistory(false);
                } else {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
        });
    }

    private void fetchHistory(boolean isRefresh) {
        String url = String.format(
                NetWork.BASE_URL,
                currentUserId, pageNum, pageSize);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            final Handler mainHandler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mainHandler.post(() -> {
                    if (isRefresh) {
                        refreshLayout.finishRefresh(false);
                    } else {
                        refreshLayout.finishLoadMore(false);
                    }
                    // 显示错误提示（可根据实际情况修改）
                    emptyView.setText("加载失败，请检查网络");
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) {
                    onFailure(call, new IOException("Response body null"));
                    return;
                }

                String json = body.string();
                // 解析通用响应 R<PageVO<PlayHistoryVO>>
                Type responseType = new TypeToken<retrofit2.Response<PageVO<PlayHistoryVO>>>() {
                }.getType();
                retrofit2.Response<PageVO<PlayHistoryVO>> result = gson.fromJson(json, responseType);

                mainHandler.post(() -> {
                    if (result == null || !result.isSuccessful()) {
                        if (isRefresh) {
                            refreshLayout.finishRefresh(false);
                        } else {
                            refreshLayout.finishLoadMore(false);
                        }
                        emptyView.setText(result != null ? result.message() : "解析失败");
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        return;
                    }

                    PageVO<PlayHistoryVO> pageData = result.body();
                    if (isRefresh) {
                        refreshLayout.finishRefresh(true);
                        historyList.clear();
                    } else {
                        refreshLayout.finishLoadMore(true);
                    }

                    if (pageData != null && pageData.getList() != null) {
                        historyList.addAll(pageData.getList());
                        adapter.notifyDataSetChanged();

                        if (pageData.getList().size() < pageSize) {
                            isLastPage = true;
                            refreshLayout.setNoMoreData(true);
                        } else {
                            refreshLayout.setNoMoreData(false);
                        }
                    }

                    // 空页面显示判断
                    if (historyList.isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}*/
/*

*/
