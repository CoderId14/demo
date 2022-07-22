package com.example.demo.config.async;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {


    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor threadPoolExecutor =new ThreadPoolTaskExecutor();
//      Hàng đợi task
        threadPoolExecutor.setQueueCapacity(30);
//      Set số thread chạy khi queue chưa đầy
        threadPoolExecutor.setCorePoolSize(2);
//      Số thread chạy khi queue đầy mà vẫn nhận thêm task
        threadPoolExecutor.setMaxPoolSize(5);
//      Set tên thread để xem log
        threadPoolExecutor.setThreadNamePrefix("send-mail-");

        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }
}
