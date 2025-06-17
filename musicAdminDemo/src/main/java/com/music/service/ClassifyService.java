package com.music.service;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.music.entity.dto.AddClassifyDTO;
import com.music.entity.dto.ModifyClassifyDTO;
import com.music.entity.dto.PageDTO;
import com.music.entity.po.ClassifyPO;
import com.music.entity.vo.*;
import com.music.mapper.ClassifyMapper;
import com.music.response.R;
import com.power.common.util.CollectionUtil;
import com.power.common.util.DateTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassifyService {

    @Autowired
    private ClassifyMapper classifyMapper;

    IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();


    public R addClassify(AddClassifyDTO dto) {
        String name = dto.getName();
        ClassifyVO classifyVO = classifyMapper.selectClassifyName(name);
        if (classifyVO != null) {
            return R.FAILED("该分类名已存在");
        }
        ClassifyPO po = new ClassifyPO();
        BeanUtils.copyProperties(dto, po);
        po.setId(identifierGenerator.nextId(new Object()).longValue());
        String createDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
        po.setCreateTime(createDateTime);
        classifyMapper.insert(po);

        return R.SUCCESS("操作成功");
    }

    public R deleteClassify(Long id) {
        classifyMapper.delete(id);
        classifyMapper.deleteClassifyMusicAll(id);

        return R.SUCCESS("操作成功");
    }

    public R classifyList() {
        List<ClassifyDataVO> result = new ArrayList<>();
        List<ClassifyDataVO> classifyListVO = classifyMapper.selectClassifyList();
        if (CollectionUtil.isEmpty(classifyListVO)) {
            return R.SUCCESS("获取成功").setData(new ArrayList<>());
        }
        List<Long> classifyIdList = classifyListVO.stream().map(ClassifyDataVO::getClassifyId).distinct().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(classifyIdList)) {
            return R.FAILED("无字典id");
        }
        List<ClassifyMusicListVO> musicListVO = classifyMapper.selectMusic(classifyIdList);
//        if (CollectionUtil.isEmpty(musicListVO)) {
//            return R.FAILED("无字典对应歌曲");
//        }
        musicListVO = musicListVO.stream().distinct().collect(Collectors.toList());
        Map<Long, Long> musicCountPerClassify = musicListVO.stream()
                .collect(Collectors.groupingBy(ClassifyMusicListVO::getClassifyId,
                        Collectors.mapping(ClassifyMusicListVO::getMusicId, Collectors.counting())));
        classifyListVO.forEach(f->{
            ClassifyDataVO vo = new ClassifyDataVO();
            BeanUtils.copyProperties(f,vo);
            Long count = musicCountPerClassify.get(f.getClassifyId());
            vo.setMusicCount(count);
            result.add(vo);
        });
        return R.SUCCESS("获取成功").setData(result);
    }

    public R modifyClassify(ModifyClassifyDTO dto) {
        String name = dto.getName();
        ClassifyVO classifyVO = classifyMapper.selectClassifyName(name);
        if (classifyVO != null) {
            return R.FAILED("该字典分类名已存在");
        }
        classifyMapper.delete(dto.getId());
        ClassifyPO po = new ClassifyPO();
        BeanUtils.copyProperties(dto, po);
        String createDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
        po.setCreateTime(createDateTime);
        classifyMapper.insert(po);

        return R.SUCCESS("操作成功");
    }
    public R getMusicByClassify(Long classifyId, String userId) {
        List<MusicVO> musicList;
        System.out.println("userId: " + userId);
        System.out.println("classifyId: " + classifyId);
        if (classifyId == null || classifyId <= 0) {
            // 获取所有音乐
            musicList = classifyMapper.selectAllMusicByUser(userId);
        } else {
            // 获取指定分类的音乐
            musicList = classifyMapper.selectMusicByClassifyAndUser(classifyId, userId);
        }
        if (musicList.isEmpty()) {
            System.out.println("No music found for user: " + userId);
        } else {
            System.out.println("Found music: " + musicList.size());
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (MusicVO m : musicList) {
            Map<String, Object> item = new HashMap<>();
            item.put("musicId", m.getId());
            item.put("musicName", m.getName());
            item.put("musicUrl", m.getUrl());
            item.put("picUrl", m.getPicUrl());
            item.put("lrcUrl", m.getLrcUrl());
            item.put("author", m.getAuthor());
            item.put("info", m.getInfo());
            item.put("year", m.getYear());
            resultList.add(item);
        }
        return R.SUCCESS("获取成功", resultList);
    }
}
