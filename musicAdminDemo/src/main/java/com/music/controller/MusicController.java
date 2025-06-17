package com.music.controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.MultipartUpload;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.music.entity.dto.ModifyMusicDTO;
import com.music.entity.dto.MusicDTO;
import com.music.entity.dto.MusicListDTO;
import com.music.response.R;
import com.music.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RequestMapping("/api/music")
@RestController
@CrossOrigin
public class MusicController {

    @Autowired
    private MusicService musicService;

    @PostMapping("/upload/mp3")
    public R uploadMusicMp3(MultipartFile file) {
        return musicService.uploadMp3(file);
    }

    @PostMapping("/upload/lrc")
    public R uploadMusicLrc(MultipartFile file) {
        return musicService.uploadLrc(file);
    }

    @PostMapping("/upload/pic")
    public R uploadMusicPic(MultipartFile file) {
        return musicService.uploadPic(file);
    }

    @PostMapping("/add")
    public R addMusic(@RequestBody MusicDTO dto){
        return musicService.addMusic(dto);
    }

    @PostMapping("/modify")
    public R modifyMusic(@RequestBody ModifyMusicDTO dto){
        return musicService.modifyMusic(dto);
    }

    @PostMapping("/list")
    public R list(@RequestBody MusicListDTO dto){
        return musicService.musicList(dto);
    }

    @PostMapping("/classify_list")
    public R getListByClassify(@RequestParam("classifyId") String classifyId){
        return musicService.getMusicListFromClassify(classifyId);
    }

    @PostMapping("/delete")
    public R deleteMusic(@RequestParam("id") Long id){
        return musicService.deleteMusic(id);
    }
}
