package com.music.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.entity.po.PlayHistoryPO;
import org.apache.ibatis.annotations.Param;

public interface PlayHistoryMapper extends BaseMapper<PlayHistoryPO> {

    /**
     * 自定义分页查询（示例）
     * @param page 分页参数
     * @param userId 用户ID
     * @return 分页结果
     */
    Page<PlayHistoryPO> selectByUser(Page<PlayHistoryPO> page, @Param("userId") Long userId);
}