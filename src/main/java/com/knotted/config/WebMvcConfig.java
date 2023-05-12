package com.knotted.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Value("${uploadPath}")
    String uploadPath; // 파일을 읽어올 경로 지정

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 경로로 들어온 요청은 uploadPath 경로를 기준으로 파일을 읽어오도록 설정함
        registry.addResourceHandler("/images/store/**")
                .addResourceLocations(uploadPath);
    }
}
