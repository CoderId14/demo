package com.example.demo.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

@Data
@EnableAsync
@Configuration
public class AppConfig {
    private List<String> authorizedRedirectUris = new ArrayList<>();
}
