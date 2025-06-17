package com.example.musicappdemo.adapter;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
    private List<String> imageUrls;

    public MyPagerAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        // 如果要实现无限轮播，返回Integer.MAX_VALUE或自定义的一个大数，然后在适配器内部处理循环逻辑
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 根据position计算实际位置，并加载对应的图片或其他内容
        int realPosition = position % imageUrls.size();
        ImageView imageView = new ImageView(container.getContext());

        // 使用Glide加载图片，并指定加载尺寸
        Glide.with(container.getContext())
                .load(imageUrls.get(realPosition))
                .into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}