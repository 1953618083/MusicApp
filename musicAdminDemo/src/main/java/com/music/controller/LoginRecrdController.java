package com.music.controller;

import com.music.entity.dto.LoginRecordDTO;
import com.music.response.R;
import com.music.service.LoginRecordService;
import org.bouncycastle.asn1.esf.SPuri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/record")
public class LoginRecrdController {

    @Autowired
    private LoginRecordService loginRecordService;

    @PostMapping("/add")
    public R addLoginRecord(@RequestBody LoginRecordDTO dto){
        return loginRecordService.addLoginRecord(dto);
    }

    @PostMapping("/logout")
    public R logout(@RequestBody LoginRecordDTO dto){
        return loginRecordService.logoutRecord(dto);
    }

    @PostMapping("/list")
    public R loginRecordList(){
        return loginRecordService.loginRecordList();
    }

}
