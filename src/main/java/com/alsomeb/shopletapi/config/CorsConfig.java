package com.alsomeb.shopletapi.config;

/*
    https://howtodoinjava.com/spring-boot2/spring-cors-configuration/
    Instead of putting @CrossOrigin in controllers

    -- Example of args --

    origins:
    List of allowed origins. Its value is placed in the Access-Control-Allow-Origin header of both pre-flight and actual responses.
    The“*” or undefined (default) means that all origins are allowed.

    allowedHeaders: list of request headers that can be used during the actual request. Its value is used in the preflight’s Access-Control-Allow-Headers response header. The “*” or undefined (default) means that all headers requested by the client are allowed.

 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*");
            }
        };
    }
}
