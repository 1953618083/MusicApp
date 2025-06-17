package com.example.musicappdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.musicappdemo.R;
import com.example.musicappdemo.entity.vo.CollectMusicVO;
import com.example.musicappdemo.entity.vo.MusicClassifyVO;

import java.util.List;

public class MusicCollectAdapter extends BaseAdapter {
    private Context context;
    private List<CollectMusicVO> mDatas;

    public MusicCollectAdapter(Context context, List<CollectMusicVO> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            //将item布局转换成view视图
            convertView = LayoutInflater.from(context).inflate(R.layout.item_singergrid,null);
            viewHodler = new ViewHodler(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        // 获取指定位置的数据
        CollectMusicVO collectMusicVO = mDatas.get(position);
        viewHodler.tv1.setText("歌曲："+collectMusicVO.getMusicName());
        viewHodler.tv2.setText("作者："+collectMusicVO.getAuthor());
        viewHodler.tv3.setText(collectMusicVO.getCreateTime());
        ViewHodler finalViewHodler = viewHodler;
        Glide.with(context)
                .asBitmap()
                .load(collectMusicVO.getPicUrl())
                .centerCrop()
                .into(new BitmapImageViewTarget(finalViewHodler.img) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.
                                        create(getView().getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(30);
                        finalViewHodler.img.setImageDrawable(circularBitmapDrawable);
                    }
                });
        return convertView;
    }

    class ViewHodler {
        TextView tv1;  //子项的文本框
        TextView tv2;  //子项的文本框
        TextView tv3;  //子项的文本框

        ImageView img;
        public ViewHodler(View view) {
            tv1 = view.findViewById(R.id.item_name);
            tv2 = view.findViewById(R.id.item_author);
            tv3 = view.findViewById(R.id.item_time);
            img = view.findViewById(R.id.musicPic);
        }
    }
}