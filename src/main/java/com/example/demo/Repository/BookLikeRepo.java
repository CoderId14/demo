package com.example.demo.Repository;

import com.example.demo.entity.BookLike;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLikeRepo extends JpaRepository<BookLike, Long> {
  Page<BookLike> findByUserId(long id,Pageable pageable);
  Optional<BookLike> findByUserIdAndAndBookId(long userid, long bookid);

}
