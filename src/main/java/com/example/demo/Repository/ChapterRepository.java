package com.example.demo.Repository;

import com.example.demo.entity.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter,Long> {

    Page<Chapter> findByBookId(Long id, Pageable pageable);

}
