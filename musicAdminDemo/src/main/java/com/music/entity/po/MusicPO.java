package com.music.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_music")
@Data
@TableName(value = "m_music")
public class MusicPO {

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "url")
    private String url;

    @TableField(value = "picUrl")
    private String picUrl;

    @TableField(value = "lrcUrl")
    private String lrcUrl;

    @TableField(value = "author")
    private String author;

    @TableField(value = "info")
    private String info;

    @TableField(value = "year")
    private String year;

    @TableField(value = "create_time")
    private String createTime;

    @TableField(value = "update_time")
    private String updateTime;

    @TableField(value = "status")
    private Integer status;
}
