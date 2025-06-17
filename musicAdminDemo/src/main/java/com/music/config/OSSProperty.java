package com.music.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "oss")
public class OSSProperty {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}