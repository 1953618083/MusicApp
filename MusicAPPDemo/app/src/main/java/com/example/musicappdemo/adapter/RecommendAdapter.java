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

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    private Context context;
    private List<MusicVO> musicList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(MusicVO musicVO);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public RecommendAdapter(Context context, List<MusicVO> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendAdapter.ViewHolder holder, int position) {
        MusicVO music = musicList.get(position);
        holder.songName.setText(music.getMusicName());
        holder.songAuthor.setText(music.getAuthor());
        holder.songClassify.setText(music.getClassifyName());
        holder.songYear.setText(music.getYear());

        Glide.with(context)
                .load(music.getPicUrl())
                .placeholder(R.drawable.music0)
                .into(holder.songPic);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(music);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView songPic, songEnter;
        TextView songName, songAuthor, songClassify, songYear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songPic = itemView.findViewById(R.id.song_pic);
            songName = itemView.findViewById(R.id.song_name);
            songAuthor = itemView.findViewById(R.id.song_author);
            songClassify = itemView.findViewById(R.id.song_classify);
            songYear = itemView.findViewById(R.id.song_year);
            songEnter = itemView.findViewById(R.id.song_enter);
        }
    }
}
