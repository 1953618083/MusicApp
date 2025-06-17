package com.music.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class UserVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private String account;

    private String password;

    private String sex;

    private Integer age;
    private String avatar;

    private String createTime;

    private String updateTime;

    private Integer status;
}
