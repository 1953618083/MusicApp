package com.example.musicappdemo.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicappdemo.R;
import com.example.musicappdemo.entity.dto.SongDTO;
import com.example.musicappdemo.listener.OnSongItemClickListener;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private final List<SongDTO> songs;
    private final OnSongItemClickListener listener;

    public SearchResultAdapter(OnSongItemClickListener context, List<SongDTO> songs, OnSongItemClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongDTO song = songs.get(position);

        // 显示歌曲名称
        holder.tvSongName.setText(song.getSongname());

        // 显示艺术家名称（假设至少有一个艺术家）
        if (song.getSinger() != null && !song.getSinger().isEmpty()) {
            holder.tvArtist.setText(song.getSinger().get(0).getName());  // 假设有一个艺术家
        } else {
            holder.tvArtist.setText("未知艺术家");
        }

        // 加载专辑封面图
        if (song.getAlbum() != null && song.getAlbum().getAlbummid() != null) {
            String albumCoverUrl = "https://y.gtimg.cn/music/photo_new/T002R300x300M000" + song.getAlbum().getAlbummid() + ".jpg";
            Glide.with(holder.itemView.getContext())
                    .load(albumCoverUrl)
                    .placeholder(R.drawable.ic_music_note)
                    .into(holder.ivAlbumCover);
        } else {
            holder.ivAlbumCover.setImageResource(R.drawable.ic_music_note);  // 如果没有专辑封面，则显示默认图标
        }

        // 设置点击事件
        holder.itemView.setOnClickListener(v -> listener.onSongItemClick(song, position));

        // 设置菜单点击事件
        holder.ivMenu.setOnClickListener(v -> listener.onSongMenuClick(song, position));
    }

    @Override
    public int getItemCount() {
        return songs != null ? songs.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumCover;
        TextView tvSongName;
        TextView tvArtist;
        ImageView ivMenu;

        ViewHolder(View itemView) {
            super(itemView);
            ivAlbumCover = itemView.findViewById(R.id.iv_album_cover);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            ivMenu = itemView.findViewById(R.id.iv_menu);
        }
    }
}
