package com.music.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class MusicDTO {

    private String name;

    private String url;

    private String picUrl;

    private String lrcUrl;

    private String author;

    private String info;

    private String year;

    private Integer status;

    private Long classifyId;

}
