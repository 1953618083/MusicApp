package com.music.mapper;

import com.music.entity.dto.UserListDTO;
import com.music.entity.po.BannerPO;
import com.music.entity.po.UserPO;
import com.music.entity.vo.BannerVO;
import com.music.entity.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BannerMapper {

    void insert(BannerPO bannerPO);

    BannerVO selectBannerById(@Param("id") Long id);

    void deleteBannerById(@Param("id") Long id);

    List<BannerVO> selectBannerList();

    void modifyBanner(@Param("bannerId") Long bannerId, @Param("status") Integer status);
}
