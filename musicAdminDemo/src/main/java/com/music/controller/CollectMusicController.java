package com.music.controller;

import com.music.entity.dto.AddCollectMusicDTO;
import com.music.entity.dto.AddLoveMusicDTO;
import com.music.mapper.CollectMusicMapper;
import com.music.response.R;
import com.music.service.CollectMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collect")
public class CollectMusicController {

    @Autowired
    private CollectMusicService collectMusicService;

    @PostMapping("/add")
    public R addCollectMusic(@RequestBody AddCollectMusicDTO dto) {
        return collectMusicService.add(dto);
    }

    @PostMapping("/list")
    public R collectMusicList(@RequestParam("account") String account) {
        return collectMusicService.collectMusicList(account);
    }

    @PostMapping("/all_list")
    public R collectMusicAllList() {
        return collectMusicService.collectMusicAllList();
    }

    @PostMapping("/musicInfo")
    public R getMusicInfo(@RequestBody AddCollectMusicDTO dto){
        return collectMusicService.getMusicInfo(dto);
    }

}
