package com.music.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.mysql.cj.log.Log;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "性别不能为空")
    private String sex;

    private String avatar;

    @NotNull(message = "年龄不能为空")
    private Integer age;

    private Integer status ;
}
