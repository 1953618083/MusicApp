package com.music.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.music.entity.vo.*;
import com.music.mapper.ClassifyMapper;
import com.music.mapper.LoveMusicMapper;
import com.music.mapper.MusicMapper;
import com.music.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RecommendService{

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private LoveMusicMapper loveMusicMapper;

    public List<MusicVO> getDailyRecommendation(Long userId) {
        List<MusicVO> result = new ArrayList<>();

        if (userId != null) {
            // 获取用户收藏最多的分类
            ClassifyVO mostFrequentClassify = classifyMapper.selectMostFrequentClassifyByUser(userId.toString());
            if (mostFrequentClassify != null) {
                String classifyName = mostFrequentClassify.getName();
                Long classifyId = getClassifyIdFromName(classifyName);
                if (classifyId != null) {
                    // 获取该分类下未收藏的推荐歌曲
                    List<MusicVO> classifyRecommend = classifyMapper.selectMusicByClassifyAndUserWithClassifyName(
                            classifyId, userId.toString());
                    result.addAll(classifyRecommend);
                }
            }
        }

        // 补全推荐：从未收藏的歌曲中随机选取
        if (result.size() < 6 && userId != null) {
            List<MusicVO> notCollectedMusic = classifyMapper.selectNotCollectedMusicByUser(userId.toString());
            Collections.shuffle(notCollectedMusic);

            // 确保不重复添加（虽然理论上不会重复）
            int need = 6 - result.size();
            for (MusicVO music : notCollectedMusic) {
                if (result.size() >= 6) break;
                if (!containsMusic(result, music)) {
                    result.add(music);
                }
            }
        }

        // 调试输出
        for (MusicVO music : result) {
            System.out.println("推荐音乐: " + music.getName() + " 分类为: " + music.getClassifyName());
        }
        return result;
    }
    // 自定义方法：检查歌曲是否已存在于列表中（基于唯一ID）
    private boolean containsMusic(List<MusicVO> list, MusicVO target) {
        for (MusicVO music : list) {
            if (music.getId().equals(target.getId())) {
                return true;
            }
        }
        return false;
    }

    private Long getClassifyIdFromName(String classifyName) {
        ClassifyVO classifyVO = classifyMapper.selectClassifyName(classifyName);
        return classifyVO != null ? classifyVO.getId() : null;
    }
}