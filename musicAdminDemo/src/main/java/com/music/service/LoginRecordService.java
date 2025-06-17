package com.music.service;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.music.entity.dto.LoginRecordDTO;
import com.music.entity.po.LoginRecordPO;
import com.music.mapper.LoginRecordMapper;
import com.music.response.R;
import com.power.common.util.CollectionUtil;
import com.power.common.util.DateTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoginRecordService {

    @Autowired
    private LoginRecordMapper loginRecordMapper;
    IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();

    public R addLoginRecord(LoginRecordDTO dto) {
        LoginRecordPO loginRecordPO = new LoginRecordPO();
        BeanUtils.copyProperties(dto,loginRecordPO);
        loginRecordPO.setId(identifierGenerator.nextId(new Object()).longValue());
        String createDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
        loginRecordPO.setCreateTime(createDateTime);
        loginRecordMapper.insert(loginRecordPO);
        return R.SUCCESS("操作成功");
    }

    public R logoutRecord(LoginRecordDTO dto) {
        LoginRecordPO logoutRecordPO = new LoginRecordPO();
        BeanUtils.copyProperties(dto,logoutRecordPO);
        logoutRecordPO.setId(identifierGenerator.nextId(new Object()).longValue());
        String createDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
        logoutRecordPO.setCreateTime(createDateTime);
        loginRecordMapper.insert(logoutRecordPO);
        return R.SUCCESS("操作成功");
    }

    public R loginRecordList() {
        List<LoginRecordPO> loginRecordPOList = loginRecordMapper.selectRecordList();
        if (CollectionUtil.isEmpty(loginRecordPOList)){
            return R.FAILED("获取成功").setData(loginRecordPOList);
        }
        return R.SUCCESS("获取成功").setData(loginRecordPOList);
    }
}
