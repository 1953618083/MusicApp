package com.music.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MusicListDTO{

    private String name;

    private String year;

    private Integer status;

}
