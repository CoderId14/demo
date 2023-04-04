package com.example.demo.Repository.user;

import com.example.demo.entity.user.UserBookHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserBookHistoryRepoCustom {

    Page<UserBookHistory> findBookReadingHistory(Long userId, Pageable pageable);
}
