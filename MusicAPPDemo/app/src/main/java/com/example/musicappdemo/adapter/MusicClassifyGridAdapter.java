package com.example.musicappdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicappdemo.R;
import com.example.musicappdemo.entity.vo.MusicClassifyVO;

import java.util.List;

public class MusicClassifyGridAdapter extends BaseAdapter {
    private Context context;
    private List<MusicClassifyVO> mDatas;

    public MusicClassifyGridAdapter(Context context, List<MusicClassifyVO> mDatas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_singergrid_classify,null);
            viewHodler = new ViewHodler(convertView);
            convertView.setTag(viewHodler);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        // 获取指定位置的数据
        MusicClassifyVO classifyVO = mDatas.get(position);
        viewHodler.tv.setText(classifyVO.getClassifyName()+"");
        return convertView;
    }

    class ViewHodler {
        TextView tv;  //子项的文本框
        public ViewHodler(View view) {
            tv = view.findViewById(R.id.item_grid_tv);
        }
    }
}