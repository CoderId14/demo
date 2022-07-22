package com.example.demo.Service;

import com.example.demo.entity.User;

public interface IConfirmationTokenService {
    String generateConfirmationToken(User user);
    Boolean isTokenValid(String token);
}
