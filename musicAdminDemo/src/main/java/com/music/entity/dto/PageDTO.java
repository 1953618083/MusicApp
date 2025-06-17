package com.music.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PageDTO {

    @NotNull(message = "每页记录数不能为空")
    private Integer pageSize;

    @NotNull(message = "页码")
    private Integer pageNum;
}
