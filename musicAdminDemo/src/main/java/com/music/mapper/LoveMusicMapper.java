package com.music.mapper;

import com.music.entity.dto.UserListDTO;
import com.music.entity.po.LoveMusicPO;
import com.music.entity.po.UserPO;
import com.music.entity.vo.LoveMusicListVO;
import com.music.entity.vo.LoveMusicVO;
import com.music.entity.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoveMusicMapper {

    void insert(LoveMusicPO po);

    LoveMusicVO selectLoveMusic(@Param("musicId") Long musicId, @Param("userId") Long userId);

    void delete(@Param("id") Long id);

    List<LoveMusicListVO> selectLoveMusicByUserId(@Param("userId") Long userId);

    List<LoveMusicListVO> selectAllLoveMusic();

}
