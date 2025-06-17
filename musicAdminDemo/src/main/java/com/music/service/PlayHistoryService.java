package com.music.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.entity.dto.PlayHistoryDTO;
import com.music.entity.po.MusicPO;
import com.music.entity.po.PlayHistoryPO;
import com.music.entity.po.UserPO;
import com.music.entity.vo.MusicVO;
//import com.music.entity.vo.PlayHistoryVO;
import com.music.entity.vo.UserVO;
import com.music.mapper.MusicMapper;
import com.music.mapper.PlayHistoryMapper;
import com.music.mapper.UserMapper;
import com.music.response.R;
import com.music.response.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayHistoryService {

    private final PlayHistoryMapper playHistoryMapper;
    private final UserMapper userMapper;
    private final MusicMapper musicMapper;

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public PlayHistoryService(PlayHistoryMapper playHistoryMapper,
                              UserMapper userMapper,
                              MusicMapper musicMapper) {
        this.playHistoryMapper = playHistoryMapper;
        this.userMapper = userMapper;
        this.musicMapper = musicMapper;
    }

    public R addPlayHistory(PlayHistoryDTO dto) {
        // 使用 selectUserById
        UserVO user = userMapper.selectUserById(dto.getUserId());
        if (user == null) {
            return R.FAILED("用户不存在");
        }

        // 使用 selectMusicById
        MusicVO music = musicMapper.selectMusicById(dto.getMusicId());
        if (music == null) {
            return R.FAILED("音乐不存在");
        }

        PlayHistoryPO po = new PlayHistoryPO();
        BeanUtils.copyProperties(dto, po);
        po.setCreateTime(LocalDateTime.now().format(TIME_FORMATTER));

        int insertResult = playHistoryMapper.insert(po);
        return insertResult > 0 ?
                R.SUCCESS("记录成功") :
                R.FAILED("记录失败");
    }

    /*public R getPlayHistoryPage(Long userId, Integer pageNum, Integer pageSize) {
        Page<PlayHistoryPO> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<PlayHistoryPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlayHistoryPO::getUserId, userId)
                .orderByDesc(PlayHistoryPO::getCreateTime);

        Page<PlayHistoryPO> poPage = playHistoryMapper.selectPage(page, queryWrapper);

        List<PlayHistoryVO> voList = poPage.getRecords().stream()
                .map(po -> {
                    PlayHistoryVO vo = new PlayHistoryVO();
                    BeanUtils.copyProperties(po, vo);

                    MusicVO music = musicMapper.selectMusicById(po.getMusicId());
                    if (music != null) {
                        vo.setMusicName(music.getName());
                        vo.setMusicAuthor(music.getAuthor());
                        vo.setMusicCover(music.getPicUrl());
                    }

                    vo.setPlayDuration(formatDuration(po.getPlayDuration()));
                    return vo;
                })
                .collect(Collectors.toList());

        PageVO<PlayHistoryVO> pageVO = new PageVO<>();
        pageVO.setList(voList);
        pageVO.setTotal(poPage.getTotal());
        pageVO.setPageNum(pageNum);
        pageVO.setPageSize(pageSize);

        return R.SUCCESS("查询成功", pageVO);
    }*/

    private String formatDuration(Integer seconds) {
        if (seconds == null || seconds < 0) return "00:00";
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}