package com.example.demo.Service.impl;

import com.example.demo.Repository.ConfirmationTokenRepo;
import com.example.demo.Service.IConfirmationTokenService;
import com.example.demo.entity.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmationTokenService implements IConfirmationTokenService {
    private final ConfirmationTokenRepo confirmationTokenRepo;


    public void saveConfirmationToken(ConfirmationToken confirmationToken){
        log.info("Save token");
        confirmationTokenRepo.save(confirmationToken);
    }


    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepo.findByToken(token);
    }

    public int setConfirmDate(String token){
        return confirmationTokenRepo.updateConfirmedDate(token, LocalDateTime.now());
    }
}
