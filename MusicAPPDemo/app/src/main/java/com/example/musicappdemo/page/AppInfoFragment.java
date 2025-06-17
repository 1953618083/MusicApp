package com.example.musicappdemo.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicappdemo.R;

public class AppInfoFragment extends Fragment {


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_app_info, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {


    }
}