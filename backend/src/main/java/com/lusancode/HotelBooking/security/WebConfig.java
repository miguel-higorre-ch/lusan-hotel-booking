package com.lusancode.HotelBooking.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = new File(uploadDir).getAbsolutePath();
        System.out.println("Serving static images from: " + absolutePath); // for debugging
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + new File(uploadDir).getAbsolutePath() + "/");
    }
}
