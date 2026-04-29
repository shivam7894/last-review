package com.tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
<<<<<<< HEAD
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://127.0.0.1:5173",
                        "http://localhost:3000",
                        "http://127.0.0.1:3000",
                        "http://localhost:5174",
                        "http://127.0.0.1:5174",
                        "http://localhost:5175",
                        "http://127.0.0.1:5175"
                )
=======
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> bcdac5e4088a6d85673b02eacfbaa20c07a73343
