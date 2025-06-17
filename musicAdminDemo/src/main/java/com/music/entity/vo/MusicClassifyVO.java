package com.music.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class MusicClassifyVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long musicId;

    private String classifyName;
}
