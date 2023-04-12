package com.example.demo.criteria.spec.book;

import com.example.demo.criteria.SearchCriteria;
import com.example.demo.criteria.SearchOperation;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.Tag;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Objects;


@Data
public class BookSpecification implements Specification<Book> {
    private final SearchCriteria searchCriteria;

    public BookSpecification(final SearchCriteria
                                     searchCriteria) {
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Book> root,
                                 CriteriaQuery<?> query, CriteriaBuilder cb) {
        String strToSearch = searchCriteria.getValue()
                .toString().toLowerCase();

        switch (Objects.requireNonNull(
                SearchOperation.getSimpleOperation
                        (searchCriteria.getOperation()))) {
            case CONTAINS:
                if (searchCriteria.getFilterKey().equals("tags")) {
                    String[] tags = strToSearch.split(",");
                    for (String tag :
                            tags) {
                        cb.like(cb.lower(tagJoin(root).
                                get("id")), tag);
                    }
                    return null;
                }
                return cb.like(cb.lower(root
                                .get(searchCriteria.getFilterKey())),
                        "%" + strToSearch + "%");

            case DOES_NOT_CONTAIN:
                if (searchCriteria.getFilterKey().equals("tags")) {
                    return cb.notLike(cb.lower(tagJoin(root).
                            <String>get("id")), strToSearch);
                }
                return cb.notLike(cb.lower(root
                                .get(searchCriteria.getFilterKey())),
                        "%" + strToSearch + "%");
            default:
                return null;
        }
    }

    private Join<Book, Tag> tagJoin(Root<Book> root) {
        return root.join("tags");
    }
}

