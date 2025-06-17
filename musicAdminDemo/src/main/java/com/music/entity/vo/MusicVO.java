package com.music.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Id;

@Data
public class MusicVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private String url;

    private String picUrl;

    private String lrcUrl;

    private String author;

    private String info;

    private String year;

    private String createTime;

    private String updateTime;

    private Integer status;

    private String classifyName;
}
