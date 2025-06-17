package com.music.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class ClassifyDataVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long classifyId;

    private String classifyName;

    private String createTime;

    private Long musicCount;

}
