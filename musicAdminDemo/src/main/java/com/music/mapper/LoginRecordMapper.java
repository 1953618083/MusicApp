package com.music.mapper;

import com.music.entity.po.BannerPO;
import com.music.entity.po.LoginRecordPO;
import com.music.entity.vo.BannerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginRecordMapper {

    void insert(LoginRecordPO loginRecordPO);

    List<LoginRecordPO> selectRecordList();


}
