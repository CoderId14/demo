package com.example.demo.Repository;

import com.example.demo.entity.BookLike;
import com.example.demo.entity.QBookLike;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface BookLikeRepo extends JpaRepository<BookLike, Long>,
        QuerydslPredicateExecutor<BookLike>, QuerydslBinderCustomizer<QBookLike>, JpaSpecificationExecutor<BookLike> {

  @Override
  default void customize(QuerydslBindings bindings, QBookLike root) {
    bindings.bind(String.class).first(
            (StringPath path, String value) -> path.containsIgnoreCase(value));
    bindings.bind(root.book.id).first(
            (path, value) -> path.eq(value)
    );
    bindings.bind(root.user.id).first(
            (path, value) -> path.eq(value)
    );
    bindings.bind(root.createdDate).all(
            (path, value) -> {
              if (value.size() == 1)
                return Optional.ofNullable(path.goe(value.iterator().next()));
              Iterator<? extends LocalDateTime> it = value.iterator();
              return Optional.ofNullable(path.between(it.next(), it.next()));
            });
    bindings.excluding(root.createdBy);
    bindings.excluding(root.modifiedBy);
  }
  Page<BookLike> findByUserId(long id,Pageable pageable);
  Optional<BookLike> findByUserIdAndAndBookId(Long userId, Long bookId);

    List<BookLike> findByUserIdAndBookIdIn(long userId, List<Long> list);
}
