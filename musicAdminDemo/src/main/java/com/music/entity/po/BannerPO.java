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
@Table(name = "m_banner")
@Data
@TableName(value = "m_banner")
public class BannerPO {

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField(value = "title")
    private String title;

    @TableField(value = "url")
    private String url;

    @TableField(value = "create_time")
    private String createTime;

    @TableField(value = "update_time")
    private String updateTime;

    @TableField(value = "status")
    private Integer status;

}
