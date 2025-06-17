package com.music.mapper;

import com.music.entity.dto.MusicDTO;
import com.music.entity.dto.MusicListDTO;
import com.music.entity.po.MusicPO;
import com.music.entity.vo.MusicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MusicMapper {
    MusicVO selectMusic(@Param("name") String name);

    void insert(MusicPO dto);

    void deleteMusicById(@Param("id") Long id);

    MusicVO selectMusicById(@Param("id") Long id);

    List<MusicVO> selectMusicList(MusicListDTO dto);

    List<Long> selectMusicFormClassifyId(@Param("classifyId")String classifyId);

    List<MusicVO> selectMusicListById(@Param("musicIdList")List<Long> musicIdList);

    @Select("SELECT * FROM m_music")
    List<MusicVO> selectAllMusic();
}
