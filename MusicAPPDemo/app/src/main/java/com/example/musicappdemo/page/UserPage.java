package com.example.musicappdemo.page;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.musicappdemo.R;
import com.example.musicappdemo.activity.LoginActivity;
import com.example.musicappdemo.music.MusicActivity;
import com.example.musicappdemo.utils.SharedPreferenceUtil;

public class UserPage extends Fragment {

    View userView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userView = inflater.inflate(R.layout.fragment_user_page, null);
        SharedPreferenceUtil.init(getContext(), "user_preference");
        initView(userView);
        return userView;
    }

    private void initView(View userView) {

        LinearLayout userInfoLayout = userView.findViewById(R.id.userInfoLayout);
        LinearLayout loveLayout = userView.findViewById(R.id.loveLayout);
        LinearLayout appInfoLayout = userView.findViewById(R.id.appInfoLayout);
        Button logoutBtn = userView.findViewById(R.id.logoutBtn);
        Button exitAppBtn = userView.findViewById(R.id.exitApp);

        userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new UserInfoFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });
        loveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new SongLoveFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });
        appInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new AppInfoFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtil.putString("account",null);
                SharedPreferenceUtil.putString("userId",null);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        exitAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().finishAffinity();
            }
        });

    }
}