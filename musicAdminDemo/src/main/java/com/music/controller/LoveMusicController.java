package com.music.controller;

import com.music.entity.dto.AddLoveMusicDTO;
import com.music.entity.dto.CancelLoveMusicDTO;
import com.music.response.R;
import com.music.service.LoveMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/love")
public class LoveMusicController {

    @Autowired
    private LoveMusicService loveMusicService;

    @PostMapping("/add")
    public R addLoveMusic(@RequestBody AddLoveMusicDTO dto) {
        return loveMusicService.add(dto);
    }

    @PostMapping("/list")
    public R loveMusicList(@RequestParam("account") String account) {
        return loveMusicService.loveMusicList(account);
    }

    @PostMapping("/all_list")
    public R loveMusicAllList() {
        return loveMusicService.loveMusicAllList();
    }

    @PostMapping("/remove")
    public R deleteLoveMusic(@RequestParam("id") Long id) {
        return loveMusicService.deleteLoveMusic(id);
    }

    @PostMapping("/musicInfo")
    public R getMusicInfo(@RequestBody AddLoveMusicDTO dto){
        return loveMusicService.getMusicInfo(dto);
    }

}
