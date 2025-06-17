package com.music.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Id;

@Data
public class BannerVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String title;

    private String url;

    private String createTime;

    private String updateTime;

    private Integer status;

}
