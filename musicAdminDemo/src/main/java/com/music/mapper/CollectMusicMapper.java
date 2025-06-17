package com.music.mapper;

import com.music.entity.dto.UserListDTO;
import com.music.entity.po.CollectMusicPO;
import com.music.entity.po.LoveMusicPO;
import com.music.entity.po.UserPO;
import com.music.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollectMusicMapper {

    void insert(CollectMusicPO po);

    CollectMusicVO selectCollectMusic(@Param("musicId") Long musicId, @Param("userId") Long userId);

    void delete(@Param("id") Long id);

    List<CollectMusicListVO> selectCollectMusicByUserId(@Param("userId") Long userId);

    List<CollectMusicListVO> selectAllCollectMusic();

}
