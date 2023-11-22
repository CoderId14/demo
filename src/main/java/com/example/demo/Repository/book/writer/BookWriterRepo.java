package com.example.demo.Repository.book.writer;

import com.example.demo.Repository.book.writer.dto.BookWriterDTO;
import com.example.demo.entity.book.BookWriterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookWriterRepo extends JpaRepository<BookWriterRequest, Long>{


    @Query(value = """
            SELECT
                bwr.id as id,
                bwr.book_id as bookId,
                bwr.user_id as userId,
                b.title as title,
                b.short_description as description,
                b.thumbnail_url as cover,
                bwr.status as status,
                u.username as username
            FROM
                tbl_book_writer_request bwr
            INNER JOIN tbl_book b ON
                b.id = bwr.book_id
            INNER JOIN tbl_user u ON
                u.id = bwr.user_id
            WHERE
                (:bookId IS NULL OR bwr.book_id = :bookId)
                AND (:id IS NULL OR bwr.id = :id)
                AND (:userId IS NULL OR bwr.user_id = :userId)
                AND (:status IS NULL OR bwr.status = :status)
""", nativeQuery = true)
    Page<BookWriterDTO> search(
            @Param("id") Long id,
            @Param("bookId") Long bookId,
            @Param("userId") Long userId,
            @Param("status") String status, Pageable pageable);
}
