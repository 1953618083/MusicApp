package com.music.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class QQMusicApiConfig {

    @Bean("qqMusicRestTemplate")
    public RestTemplate qqMusicRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri("http://localhost:3200")
                .setConnectTimeout(Duration.ofSeconds(5))
                .additionalInterceptors(new LoggingInterceptor())
                .build();
    }
}