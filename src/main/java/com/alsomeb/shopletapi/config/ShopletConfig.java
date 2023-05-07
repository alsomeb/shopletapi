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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@Configuration
public class ShopletConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("my.gmail@gmail.com"); // dold
        mailSender.setPassword("password"); // dold

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
