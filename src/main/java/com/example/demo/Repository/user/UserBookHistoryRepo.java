package com.example.demo.Repository.user;

import com.example.demo.entity.user.UserBookHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBookHistoryRepo extends JpaRepository<UserBookHistory, Long>, UserBookHistoryRepoCustom {
    Optional<UserBookHistory> findByUser_IdAndBook_IdAndChapter_Id(long userId, long bookId, long chapterId);

}
