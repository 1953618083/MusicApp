package com.music.service;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.pagehelper.PageHelper;
import com.music.entity.dto.AddLoveMusicDTO;
import com.music.entity.dto.CancelLoveMusicDTO;
import com.music.entity.po.LoveMusicPO;
import com.music.entity.vo.LoveMusicListVO;
import com.music.entity.vo.LoveMusicVO;
import com.music.entity.vo.MusicVO;
import com.music.entity.vo.UserVO;
import com.music.mapper.LoveMusicMapper;
import com.music.mapper.MusicMapper;
import com.music.mapper.UserMapper;
import com.music.response.R;
import com.power.common.util.CollectionUtil;
import com.power.common.util.DateTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoveMusicService {

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private LoveMusicMapper loveMusicMapper;

    @Autowired
    private UserMapper userMapper;
    IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();

    public R add(AddLoveMusicDTO dto) {

        Long musicId = dto.getMusicId();
        Long userId = dto.getUserId();
        LoveMusicVO loveMusicVO = loveMusicMapper.selectLoveMusic(musicId, userId);
        MusicVO musicVO = musicMapper.selectMusicById(musicId);
        if (musicVO == null) {
            return R.FAILED("该歌曲id不存在!");
        }
        UserVO userVO = userMapper.selectUserById(userId);
        if (userVO == null) {
            return R.FAILED("该用户id不存在！");
        }
        Map<String, String> map = new HashMap<>();
        if (loveMusicVO == null) {
            LoveMusicPO po = new LoveMusicPO();
            BeanUtils.copyProperties(dto, po);
            po.setId(identifierGenerator.nextId(new Object()).longValue());
            String createDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
            po.setCreateTime(createDateTime);
            loveMusicMapper.insert(po);
            map.put("status", "1");
        } else {
            loveMusicMapper.delete(loveMusicVO.getId());
            map.put("status", "0");
        }


        return R.SUCCESS("操作成功").setData(map);
    }

    public R loveMusicList(String account) {
        UserVO userVO = userMapper.selectUserByAccount(account);
        if (userVO == null) {
            return R.FAILED("该用户不存在!");
        }
        List<LoveMusicListVO> loveMusicVOList = loveMusicMapper.selectLoveMusicByUserId(userVO.getId());

        return R.SUCCESS("获取成功").setData(loveMusicVOList);
    }


    public R loveMusicAllList() {
        List<LoveMusicListVO> result = new ArrayList<>();
        List<LoveMusicListVO> loveMusicVOList = loveMusicMapper.selectAllLoveMusic();
        if (CollectionUtil.isEmpty(loveMusicVOList)) {
            return R.SUCCESS("获取成功").setData(new ArrayList<>());
        }
        List<Long> userIdList = loveMusicVOList.stream().map(LoveMusicListVO::getUserId).distinct().collect(Collectors.toList());
        List<UserVO> userInfo = userMapper.selectUserInfo(userIdList);
        Map<Long, String> userMap = userInfo.stream().distinct().collect(Collectors.toMap(UserVO::getId, UserVO::getName));
        loveMusicVOList.forEach(f -> {
            LoveMusicListVO vo = new LoveMusicListVO();
            BeanUtils.copyProperties(f, vo);
            vo.setUserName(userMap.get(f.getUserId()));
            result.add(vo);
        });

        return R.SUCCESS("获取成功").setData(result);

    }

    public R deleteLoveMusic(Long id) {
        loveMusicMapper.delete(id);
        return R.SUCCESS("操作成功");
    }

    public R getMusicInfo(AddLoveMusicDTO dto) {
        Long musicId = dto.getMusicId();
        Long userId = dto.getUserId();
        LoveMusicVO loveMusicVO = loveMusicMapper.selectLoveMusic(musicId, userId);
        MusicVO musicVO = musicMapper.selectMusicById(musicId);
        if (musicVO == null) {
            return R.FAILED("该歌曲id不存在!");
        }
        UserVO userVO = userMapper.selectUserById(userId);
        if (userVO == null) {
            return R.FAILED("该用户id不存在！");
        }
        Map<String, String> map = new HashMap<>();
        if (loveMusicVO == null) {
            map.put("status", "0");
        } else {
            map.put("status", "1");
        }
        return R.SUCCESS("操作成功").setData(map);
    }
}
