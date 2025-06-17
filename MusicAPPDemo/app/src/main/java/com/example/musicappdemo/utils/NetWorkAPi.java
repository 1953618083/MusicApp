package com.example.musicappdemo.utils;

import android.net.Network;

public interface NetWorkAPi {

     String doLogin = NetWork.BASE_URL + "/api/user/login";
     String doRegister = NetWork.BASE_URL + "/api/user/register";
     String userInfo = NetWork.BASE_URL + "/api/user/user_info?account=";
     String musicList = NetWork.BASE_URL + "/api/music/list";
     String getClassifyMusic = NetWork.BASE_URL + "/api/music/classify_list?classifyId=";
     String initMusicClassifyList = NetWork.BASE_URL + "/api/classify/list";
     String initMusicCollectList = NetWork.BASE_URL + "/api/collect/list?account=";
     String initMusicLoveList = NetWork.BASE_URL + "/api/love/list?account=";
     String loveMusicInfo = NetWork.BASE_URL + "/api/love/musicInfo";
     String addLoveMusic = NetWork.BASE_URL + "/api/love/add";
     String collectMusicInfo = NetWork.BASE_URL + "/api/collect/musicInfo";
     String addCollectMusic = NetWork.BASE_URL + "/api/collect/add";
     String uploadAvatar = NetWork.BASE_URL + "/api/music/upload/pic";

     String classifyList = NetWork.BASE_URL + "/api/classify/list";

     String musicByClassify = NetWork.BASE_URL + "/api/classify/";

     String MusicRecommend = NetWork.BASE_URL + "/api/recommend/daily";
}
