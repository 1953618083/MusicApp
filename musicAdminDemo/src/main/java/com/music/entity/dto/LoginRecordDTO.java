package com.music.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class LoginRecordDTO {

    private String loginAccount;

    private Integer status;
}
