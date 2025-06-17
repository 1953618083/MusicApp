package com.music;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@MapperScan("com.music.mapper")
@ComponentScans(value = {@ComponentScan("com.music.config"),@ComponentScan("com.music.utils")})
public class MusicApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class,args);
    }
}
