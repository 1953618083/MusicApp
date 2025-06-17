package com.music.service;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.music.entity.dto.BannerDTO;
import com.music.entity.dto.PageDTO;
import com.music.entity.po.BannerPO;
import com.music.entity.vo.BannerVO;
import com.music.entity.vo.UserVO;
import com.music.mapper.BannerMapper;
import com.music.response.R;
import com.power.common.util.CollectionUtil;
import com.power.common.util.DateTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();

    public R addBanner(BannerDTO dto) {

        if (dto.getId() == null) {

            BannerPO bannerPO = new BannerPO();
            BeanUtils.copyProperties(dto, bannerPO);
            bannerPO.setId(identifierGenerator.nextId(new Object()).longValue());
            String createDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
            String updateDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
            bannerPO.setCreateTime(createDateTime);
            bannerPO.setUpdateTime(updateDateTime);
            bannerMapper.insert(bannerPO);
        } else {

            BannerPO bannerPO = new BannerPO();
            BeanUtils.copyProperties(dto,bannerPO);
            String updateDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
            BannerVO bannerVO = bannerMapper.selectBannerById(dto.getId());
            bannerPO.setUpdateTime(updateDateTime);
            bannerPO.setCreateTime(bannerVO.getCreateTime());
            bannerMapper.deleteBannerById(dto.getId());
            bannerMapper.insert(bannerPO);
        }


        return R.SUCCESS("操作成功!");
    }


    public R list() {

        List<BannerVO> bannerVOList = bannerMapper.selectBannerList();
        if (CollectionUtil.isEmpty(bannerVOList)){
            return R.SUCCESS("获取成功").setData(new ArrayList<>());
        }
        return R.SUCCESS("获取成功").setData(bannerVOList);
    }

    public R remove(Long bannerId) {
        bannerMapper.deleteBannerById(bannerId);
        return R.SUCCESS("操作成功");
    }

    public R stop(Long bannerId) {
        BannerVO bannerVO = bannerMapper.selectBannerById(bannerId);
        if (bannerVO==null){
            return R.FAILED("该轮播图不存在!");
        }
        Integer status = bannerVO.getStatus();
        if (status == 1) {
            status = 0;
        } else {
            status = 1;
        }
        bannerMapper.modifyBanner(bannerId,status);
        return R.SUCCESS("操作成功");
    }
}
