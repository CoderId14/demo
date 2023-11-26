package com.example.demo.Repository.user.writer;

import com.example.demo.Repository.user.writer.dto.UserPromoteDTO;
import com.example.demo.entity.book.WriterRequestStatus;
import com.example.demo.entity.user.UserPromoteRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPromoteRequestRepo extends JpaRepository<UserPromoteRequest, Long> {

    @Query(value = """
            SELECT
                upr.id as id,
                upr.user_id as userId,
                u.name as fullName,
                upr.role_id as roleId,
                r.role_name as roleName,
                upr.status as status
            FROM
                tbl_user_promote_request upr
            INNER JOIN tbl_user u ON
                u.id = upr.user_id
            INNER JOIN tbl_role r ON
                r.id = upr.role_id
            WHERE
                (:id IS NULL OR upr.id = :id)
                AND (:userId IS NULL OR upr.user_id = :userId)
                AND (:roleId IS NULL OR upr.role_id = :roleId)
                AND (:status IS NULL OR upr.status = :status)
""", nativeQuery = true)
    Page<UserPromoteDTO> search(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("roleId") Long roleId,
            @Param("status") String status,
            Pageable pageable);

    Page<UserPromoteRequest> searchAllByStatus(WriterRequestStatus status, Pageable pageable);
}
