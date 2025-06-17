package com.music.controller;


import com.music.entity.dto.BannerDTO;
import com.music.entity.dto.PageDTO;
import com.music.response.R;
import com.music.service.BannerService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/banner")
@RestController
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @PostMapping("/add")
    public R addBanner(@RequestBody BannerDTO dto){
        return bannerService.addBanner(dto);
    }

    @PostMapping("/list")
    public R bannerList(){
        return bannerService.list();
    }

    @PostMapping("/delete")
    public R removeBanner(@RequestParam("id") Long bannerId){
        return bannerService.remove(bannerId);
    }

    @PostMapping("/stop")
    public R stopBanner(@RequestParam("bannerId") Long bannerId){
        return bannerService.stop(bannerId);
    }
}
