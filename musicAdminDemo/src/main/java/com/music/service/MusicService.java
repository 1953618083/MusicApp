package com.music.service;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.music.entity.dto.ModifyMusicDTO;
import com.music.entity.dto.MusicDTO;
import com.music.entity.dto.MusicListDTO;
import com.music.entity.po.ClassifyMusicPO;
import com.music.entity.po.MusicPO;
import com.music.entity.vo.MusicClassifyVO;
import com.music.entity.vo.MusicVO;
import com.music.mapper.ClassifyMapper;
import com.music.mapper.MusicMapper;
import com.music.response.R;
import com.music.utils.OSSUtils;
import com.power.common.util.CollectionUtil;
import com.power.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MusicService {

    @Autowired
    private OSSUtils ossUtils;

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private ClassifyMapper classifyMapper;



    IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();


    public R uploadMp3(MultipartFile file) {
        String fileUrl = ossUtils.uploadFile(file, "mp3");
        return R.SUCCESS("音乐mp3上传成功").setData(fileUrl);
    }

    public R uploadLrc(MultipartFile file) {
        String fileUrl = ossUtils.uploadFile(file, "lrc");
        return R.SUCCESS("音乐lrc上传成功").setData(fileUrl);
    }

    public R uploadPic(MultipartFile file) {
        String fileUrl = ossUtils.uploadFile(file, "pic");
        return R.SUCCESS("音乐pic上传成功").setData(fileUrl);
    }

    public R addMusic(MusicDTO dto) {
        String name = dto.getName();
        String url = dto.getUrl();
        String picUrl = dto.getPicUrl();
        String lrcUrl = dto.getLrcUrl();
        String author = dto.getAuthor();
        String info = dto.getInfo();
        String year = dto.getYear();
        Long classifyId = dto.getClassifyId();
        if (StringUtils.isEmpty(name)) {
            return R.FAILED("歌曲名不能为空！");
        }
        if (StringUtils.isEmpty(url)) {
            return R.FAILED("歌曲链接不能为空！");
        }
        if (StringUtils.isEmpty(picUrl)) {
            return R.FAILED("歌曲封面不能为空！");
        }
        if (StringUtils.isEmpty(lrcUrl)) {
            return R.FAILED("歌词不能为空！");
        }
        if (StringUtils.isEmpty(author)) {
            return R.FAILED("歌曲作者不能为空！");
        }
        if (StringUtils.isEmpty(info)) {
            return R.FAILED("歌曲信息不能为空！");
        }
        if (StringUtils.isEmpty(year)) {
            return R.FAILED("发行年份不能为空！");
        }
        MusicVO musicVO = musicMapper.selectMusic(name);
        if (musicVO != null) {
            return R.FAILED("该歌曲已存在");
        }
        MusicPO po = new MusicPO();
        BeanUtils.copyProperties(dto, po);
        po.setId(identifierGenerator.nextId(new Object()).longValue());
        String createDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
        String updateDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
        po.setCreateTime(createDateTime);
        po.setUpdateTime(updateDateTime);
        musicMapper.insert(po);

        ClassifyMusicPO classifyMusicPO = new ClassifyMusicPO();
        classifyMusicPO.setClassifyId(classifyId);
        classifyMusicPO.setMusicId(po.getId());
        classifyMusicPO.setId(identifierGenerator.nextId(new Object()).longValue());
        classifyMapper.insertClassifyMusic(classifyMusicPO);
        return R.SUCCESS("操作成功");
    }

    public R modifyMusic(ModifyMusicDTO dto) {
        Long id = dto.getId();
        String name = dto.getName();
        String url = dto.getUrl();
        String picUrl = dto.getPicUrl();
        String lrcUrl = dto.getLrcUrl();
        String author = dto.getAuthor();
        String info = dto.getInfo();
        String year = dto.getYear();
        if (id == null) {
            return R.FAILED("歌曲id不能为空！");
        }
        if (StringUtils.isEmpty(name)) {
            return R.FAILED("歌曲名不能为空！");
        }
        if (StringUtils.isEmpty(url)) {
            return R.FAILED("歌曲链接不能为空！");
        }
        if (StringUtils.isEmpty(picUrl)) {
            return R.FAILED("歌曲封面不能为空！");
        }
        if (StringUtils.isEmpty(lrcUrl)) {
            return R.FAILED("歌词不能为空！");
        }
        if (StringUtils.isEmpty(author)) {
            return R.FAILED("歌曲作者不能为空！");
        }
        if (StringUtils.isEmpty(info)) {
            return R.FAILED("歌曲信息不能为空！");
        }
        if (StringUtils.isEmpty(year)) {
            return R.FAILED("发行年份不能为空！");
        }
        MusicVO musicVO = musicMapper.selectMusicById(id);
        if (musicVO == null) {
            return R.FAILED("该歌曲不存在");
        }
        MusicPO po = new MusicPO();
        BeanUtils.copyProperties(dto, po);
        String updateDateTime = DateTimeUtil.dateToStr(new Date(), DateTimeUtil.DATE_FORMAT_SECOND);
        po.setCreateTime(musicVO.getCreateTime());
        po.setUpdateTime(updateDateTime);
        musicMapper.deleteMusicById(id);
        musicMapper.insert(po);

        return R.SUCCESS("操作成功");
    }

    public R musicList(MusicListDTO dto) {
        List<MusicVO> result = new ArrayList<>();
        List<MusicVO> musicVOList = musicMapper.selectMusicList(dto);
        if (CollectionUtil.isEmpty(musicVOList)) {
            return R.SUCCESS("获取成功").setData(new ArrayList<>());
        }
        List<Long> musicIdList = musicVOList.stream().distinct().map(MusicVO::getId).collect(Collectors.toList());
        List<MusicClassifyVO> classifyVOList = classifyMapper.selectClassifyByMusic(musicIdList);
        Map<Long, String> musicNameMap = classifyVOList.stream().distinct().collect(Collectors.toMap(MusicClassifyVO::getMusicId, MusicClassifyVO::getClassifyName));
        musicVOList.forEach(f -> {
            MusicVO vo = new MusicVO();
            BeanUtils.copyProperties(f, vo);
            vo.setClassifyName(musicNameMap.get(f.getId()));
            result.add(vo);
        });
        return R.SUCCESS("获取成功").setData(result);
    }

    public R deleteMusic(Long id) {
        musicMapper.deleteMusicById(id);

        return R.SUCCESS("操作成功");
    }

    public R getMusicListFromClassify(String classifyId) {
        List<Long> musicIdList = musicMapper.selectMusicFormClassifyId(classifyId);
        if (CollectionUtil.isEmpty(musicIdList)){
            return R.FAILED("该分类下音乐为空!").setData(new ArrayList<>());
        }
        musicIdList = musicIdList.stream().distinct().collect(Collectors.toList());
        List<MusicVO> musicVOList = musicMapper.selectMusicListById(musicIdList);
        musicVOList =  musicVOList.stream().distinct().collect(Collectors.toList());
        List<MusicClassifyVO> classifyVOList = classifyMapper.selectClassifyByMusic(musicIdList);
        Map<Long, String> musicNameMap = classifyVOList.stream().distinct().collect(Collectors.toMap(MusicClassifyVO::getMusicId, MusicClassifyVO::getClassifyName));
        List<MusicVO> result = new ArrayList<>();
        musicVOList.forEach(f -> {
            MusicVO vo = new MusicVO();
            BeanUtils.copyProperties(f, vo);
            vo.setClassifyName(musicNameMap.get(f.getId()));
            result.add(vo);
        });
        return R.SUCCESS("获取成功").setData(result);
    }
}
