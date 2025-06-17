package com.music.entity.dto;

import lombok.Data;

@Data
public class ModifyMusicDTO {
    private Long id;

    private String name;

    private String url;

    private String picUrl;

    private String lrcUrl;

    private String author;

    private String info;

    private String year;

    private Integer status;
}
