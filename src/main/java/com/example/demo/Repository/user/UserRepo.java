package com.example.demo.Repository.user;

import com.example.demo.entity.user.QUser;
import com.example.demo.entity.user.User;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {
    @Override
    default void customize(QuerydslBindings bindings, QUser root) {

        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.createdBy);
        bindings.excluding(root.modifiedBy);
    }

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query(value = """
            SELECT DISTINCT 
                *
            FROM tbl_user u
            INNER JOIN tbl_user_role ur ON u.id = ur.user_id
            INNER JOIN tbl_role r ON ur.role_id = r.id
            WHERE (:username IS NULL OR u.username LIKE CONCAT('%',:username,'%'))
            AND (:id IS NULL OR u.id = :id)
            AND (:email IS NULL OR u.email LIKE CONCAT('%',:email,'%'))
            AND (:name IS NULL OR u.name LIKE CONCAT('%',:name,'%'))
            AND (:phoneNumber IS NULL OR u.phone_number LIKE CONCAT('%',:phoneNumber,'%'))
            AND (:role IS NULL OR r.role_name = :role)
            AND (:isActive IS NULL OR u.is_active = :isActive)
""",nativeQuery = true)
    Page<User> searchUser(
            @Param("id") Long id,
            @Param("username") String username,
            @Param("email") String email,
            @Param("name") String name,
            @Param("phoneNumber") String phoneNumber,
            @Param("role") String role,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );


}
