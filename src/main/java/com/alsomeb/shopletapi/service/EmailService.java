package com.alsomeb.shopletapi.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
