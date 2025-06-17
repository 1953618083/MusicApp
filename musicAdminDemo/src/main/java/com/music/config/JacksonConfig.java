package com.music.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 配置日期格式为 ISO 8601
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new JavaTimeModule());

        return objectMapper;
    }
}
