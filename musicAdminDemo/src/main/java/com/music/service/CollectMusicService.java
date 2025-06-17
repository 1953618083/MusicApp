package com.music.service;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.music.entity.dto.AddCollectMusicDTO;
import com.music.entity.dto.AddLoveMusicDTO;
import com.music.entity.po.CollectMusicPO;
import com.music.entity.po.LoveMusicPO;
import com.music.entity.vo.*;
import com.music.mapper.CollectMusicMapper;
import com.music.mapper.LoveMusicMapper;
import com.music.mapper.MusicMapper;
import com.music.mapper.UserMapper;
import com.music.response.R;
import com.power.common.util.DateTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollectMusicService {

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private CollectMusicMapper collectMusicMapper;

    @Autowired
    private UserMapper userMapper;

    IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();


    public R add(AddCollectMusicDTO dto) {
        Long musicId = dto.getMusicId();
        Long userId = dto.getUserId();
        CollectMusicVO collectMusicVO = collectMusicMapper.selectCollectMusic(musicId, userId);
        MusicVO musicVO = musicMapper.selectMusicById(musicId);
        if (musicVO == null) {
            return R.FAILED("该歌曲id不存在!");
        }
        UserVO userVO = userMapper.selectUserById(userId);
        if (userVO == null) {
            return R.FAILED("该用户id不存在！");
        }
        Map<String, String> map = new HashMap<>();

        if (collectMusicVO == null) {
            CollectMusicPO po = new CollectMusicPO();
            BeanUtils.copyProperties(dto, po);
            po.setId(identifierGenerator.nextId(new Object()).longValue());
            String createDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
            po.setCreateTime(createDateTime);
            collectMusicMapper.insert(po);
            map.put("status", "1");

        } else {
            collectMusicMapper.delete(collectMusicVO.getId());
            map.put("status", "0");

        }


        return R.SUCCESS("操作成功").setData(map);
    }

    public R collectMusicList(String account) {
        UserVO userVO = userMapper.selectUserByAccount(account);
        if (userVO == null) {
            return R.FAILED("该用户不存在!");
        }
        List<CollectMusicListVO> collectMusicListVOList = collectMusicMapper.selectCollectMusicByUserId(userVO.getId());

        return R.SUCCESS("获取成功").setData(collectMusicListVOList);
    }

    public R collectMusicAllList() {
        List<CollectMusicListVO> collectMusicListVOList = collectMusicMapper.selectAllCollectMusic();

        return R.SUCCESS("获取成功").setData(collectMusicListVOList);
    }

    public R getMusicInfo(AddCollectMusicDTO dto) {
        Long musicId = dto.getMusicId();
        Long userId = dto.getUserId();
        CollectMusicVO collectMusicVO = collectMusicMapper.selectCollectMusic(musicId, userId);
        MusicVO musicVO = musicMapper.selectMusicById(musicId);
        if (musicVO == null) {
            return R.FAILED("该歌曲id不存在!");
        }
        UserVO userVO = userMapper.selectUserById(userId);
        if (userVO == null) {
            return R.FAILED("该用户id不存在！");
        }
        Map<String, String> map = new HashMap<>();
        if (collectMusicVO == null) {
            map.put("status", "0");
        } else {
            map.put("status", "1");
        }
        return R.SUCCESS("操作成功").setData(map);
    }
}
