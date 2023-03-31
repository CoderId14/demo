package com.example.demo.Repository.category;

import com.example.demo.entity.Category;
import com.example.demo.entity.QCategory;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long>,
        QuerydslPredicateExecutor<Category>, QuerydslBinderCustomizer<QCategory> {
    @Override
    default void customize(QuerydslBindings bindings, QCategory root) {
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.createdBy);
        bindings.excluding(root.modifiedBy);
    }

    Optional<Category> findByName(String name);

}
