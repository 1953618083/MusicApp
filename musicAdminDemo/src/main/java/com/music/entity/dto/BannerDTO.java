package com.music.entity.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class BannerDTO {

    private Long id;

    private String title;

    private String url;

    private Integer status;

}
