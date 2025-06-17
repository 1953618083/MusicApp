package com.music.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CollectMusicListVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long musicId;

    private String createTime;

    private String musicName;

    private String musicUrl;

    private String picUrl;

    private String lrcUrl;

    private String author;

    private String year;
}
