package com.example.demo.Service.confirmation;

import com.example.demo.entity.user.User;

public interface IConfirmationTokenService {
    String generateConfirmationToken(User user);
    Boolean isTokenValid(String token);
}
