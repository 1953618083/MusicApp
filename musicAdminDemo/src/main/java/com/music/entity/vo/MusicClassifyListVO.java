package com.music.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class MusicClassifyListVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long classifyId;

    private String classifyName;

    private Integer musicCount;

    private String createTime;
}
