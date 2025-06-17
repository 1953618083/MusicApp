package com.music.controller;

import com.music.entity.vo.MusicVO;
import com.music.entity.vo.UserVO;
import com.music.response.R;
import com.music.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @GetMapping("/daily")
    public R getDailyRecommend(@RequestParam(required = false) Long userId) {
        List<MusicVO> recommendList = recommendService.getDailyRecommendation(userId);
        return R.SUCCESS("获取每日推荐成功", recommendList);
    }
}
