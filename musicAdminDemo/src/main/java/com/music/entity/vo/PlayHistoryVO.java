package com.music.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class PlayHistoryVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String musicName;
    private String musicAuthor;
    private String musicCover;
    private String createTime;
    private String playDuration; // 格式化的播放时长

    // 关联用户信息
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private String userName;
}