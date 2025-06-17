package com.music.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "m_user")
@Data
@TableName(value = "m_user")
public class UserPO {

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "account")
    private String account;

    @TableField(value = "password")
    private String password;

    @TableField(value = "sex")
    private String sex;


    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "age")
    private Integer age;

    @TableField(value = "create_time")
    private String createTime;

    @TableField(value = "update_time")
    private String updateTime;

    @TableField(value = "status")
    private Integer status;
}
