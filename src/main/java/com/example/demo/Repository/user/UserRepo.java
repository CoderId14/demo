package com.example.demo.Repository.user;

import com.example.demo.Repository.book.BookRepoCustom;
import com.example.demo.entity.user.QUser;
import com.example.demo.entity.user.User;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

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


}
