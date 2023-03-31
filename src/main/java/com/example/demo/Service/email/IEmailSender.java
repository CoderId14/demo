package com.example.demo.Service.email;

public interface IEmailSender {
    void send(String to, String email);
    void receive(String from, Object object);
}
