package com.music.mapper;

import com.music.entity.po.ClassifyMusicPO;
import com.music.entity.po.ClassifyPO;
import com.music.entity.po.CollectMusicPO;
import com.music.entity.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassifyMapper {


    ClassifyVO selectClassifyName(@Param("name") String name);

    void insert(ClassifyPO po);

    void delete(@Param("id") Long id);

    void deleteClassifyMusicAll(@Param("id") Long id);

    List<ClassifyDataVO> selectClassifyList();

    List<ClassifyMusicListVO> selectMusic(@Param("classifyIdList") List<Long> classifyIdList);

    void insertClassifyMusic(ClassifyMusicPO classifyMusicPO);

    List<MusicClassifyVO> selectClassifyByMusic(@Param("musicIdList")List<Long> musicIdList);
    @Select("SELECT m.* FROM m_music m " +
            "JOIN m_collect_music cm ON m.id = cm.music_id " +
            "WHERE cm.user_id = #{userId}")
    List<MusicVO> selectAllMusicByUser(@Param("userId") String userId);

    @Select("SELECT m.* FROM m_music m " +
            "JOIN m_classify_music cm ON m.id = cm.music_id " +
            "JOIN m_collect_music col ON m.id = col.music_id " +
            "WHERE cm.classify_id = #{classifyId} AND col.user_id = #{userId}")
    List<MusicVO> selectMusicByClassifyAndUser(
            @Param("classifyId") Long classifyId,
            @Param("userId") String userId
    );
    @Select("SELECT name FROM classify WHERE id = #{id}")
    String getNameById(@Param("id") Long id);

    // 新增：带 classifyName 的用户所有收藏音乐查询
    @Select("SELECT m.*, c.name AS classifyName FROM m_music m " +
            "JOIN m_collect_music cm ON m.id = cm.music_id " +
            "LEFT JOIN m_classify_music mcm ON m.id = mcm.music_id " +
            "LEFT JOIN m_classify c ON mcm.classify_id = c.id " +
            "WHERE cm.user_id = #{userId}")
    List<MusicVO> selectAllMusicWithClassifyName(@Param("userId") String userId);

    // 新增：带 classifyName 的分类下推荐
    @Select("SELECT m.*, c.name AS classifyName FROM m_music m " +
            "JOIN m_classify_music mcm ON m.id = mcm.music_id " +
            "JOIN m_collect_music col ON m.id = col.music_id " +
            "LEFT JOIN m_classify c ON mcm.classify_id = c.id " +
            "WHERE mcm.classify_id = #{classifyId} AND col.user_id = #{userId}")
    List<MusicVO> selectMusicByClassifyAndUserWithClassifyName(
            @Param("classifyId") Long classifyId,
            @Param("userId") String userId
    );

    //寻找与收藏不同的歌曲
    @Select("SELECT m.*, c.name AS classifyName FROM m_music m " +
            "LEFT JOIN m_collect_music cm ON m.id = cm.music_id AND cm.user_id = #{userId} " +
            "LEFT JOIN m_classify_music mcm ON m.id = mcm.music_id " +
            "LEFT JOIN m_classify c ON mcm.classify_id = c.id " +
            "WHERE cm.music_id IS NULL")
    List<MusicVO> selectNotCollectedMusicByUser(@Param("userId") String userId);

    @Select("SELECT c.name AS classifyName, COUNT(*) AS count " +
            "FROM m_classify_music mcm " +
            "JOIN m_collect_music cm ON mcm.music_id = cm.music_id " +
            "JOIN m_classify c ON mcm.classify_id = c.id " +
            "WHERE cm.user_id = #{userId} " +
            "GROUP BY c.name " +
            "ORDER BY count DESC " +
            "LIMIT 1")
    ClassifyVO selectMostFrequentClassifyByUser(@Param("userId") String userId);
}
