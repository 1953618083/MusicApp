package com.music.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class ClassifyMusicListVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long classifyId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long musicId;
}
