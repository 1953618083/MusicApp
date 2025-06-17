/*
package com.example.musicappdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicappdemo.R;
import com.example.musicappdemo.entity.vo.MusicVO;
import com.example.musicappdemo.entity.vo.PlayHistoryVO;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final List<PlayHistoryVO> historyList;
    private Context context;

    public HistoryAdapter(Context context, List<PlayHistoryVO> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_play_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        PlayHistoryVO history = historyList.get(position);

        holder.tvMusicName.setText(history.getMusicName());
        holder.tvAuthor.setText("作者：" + history.getMusicAuthor());
        holder.tvDuration.setText("时长：" + history.getPlayDuration());
        holder.tvCreateTime.setText("播放时间：" + history.getCreateTime());

        Glide.with(holder.itemView.getContext())
                .load(history.getMusicCover())
                .placeholder(R.drawable.ic_music_placeholder)
                .into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvMusicName, tvAuthor, tvDuration, tvCreateTime;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvMusicName = itemView.findViewById(R.id.tvMusicName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvCreateTime = itemView.findViewById(R.id.tvCreateTime);
        }
    }
}

*/
