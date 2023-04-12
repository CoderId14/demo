package com.example.demo.criteria.spec.book;

import com.example.demo.criteria.SearchCriteria;
import com.example.demo.criteria.SearchOperation;
import com.example.demo.entity.book.Book;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecificationBuilder {
    private final List<SearchCriteria> params;

    public BookSpecificationBuilder(){
        this.params = new ArrayList<>();
    }

    public final BookSpecificationBuilder with(String key,
                                              String operation, Object value){
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public final BookSpecificationBuilder with(SearchCriteria
                                                      searchCriteria){
        params.add(searchCriteria);
        return this;
    }

    public Specification<Book> build(){
        if(params.size() == 0){
            return null;
        }

        Specification<Book> result =
                new BookSpecification(params.get(0));
        for (int idx = 1; idx < params.size(); idx++){
            SearchCriteria criteria = params.get(idx);
            result =  SearchOperation.getDataOption(criteria
                    .getDataOption()) == SearchOperation.ALL
                    ? Specification.where(result).and(new
                    BookSpecification(criteria))
                    : Specification.where(result).or(
                    new BookSpecification(criteria));
        }
        return result;
    }
}
