package com.example.demo.Service.impl;

import com.example.demo.Repository.ConfirmationTokenRepo;
import com.example.demo.Service.IConfirmationTokenService;
import com.example.demo.entity.ConfirmationToken;
import com.example.demo.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmationTokenService implements IConfirmationTokenService {
    private final ConfirmationTokenRepo confirmationTokenRepo;



    public String generateConfirmationToken(User user){
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .expireDate(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        confirmationTokenRepo.save(confirmationToken);
        return token;
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepo.findByToken(token);
    }

    public int setConfirmDate(String token){
        return confirmationTokenRepo.updateConfirmedDate(token, LocalDateTime.now());
    }
    public Boolean isTokenValid(String token){
        ConfirmationToken confirmationToken = this.getToken(token).orElseThrow(
                () -> new IllegalStateException("Token not found")
        );

        if(confirmationToken.getConfirmedDate() != null){
            log.error("token already use");
            return true;
        }

        LocalDateTime expiredDate = confirmationToken.getExpireDate();
        if(expiredDate.isBefore(LocalDateTime.now())){
            log.error("Token has expired");
            return true;
        }

        return false;
    }
}
