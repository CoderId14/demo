package com.example.demo.criteria;

import com.example.demo.entity.QBook;
import com.example.demo.entity.Tag;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class BookPredicateBuilder {
    private final List<BooleanBuilder> expressions = new ArrayList<>();

    public BookPredicateBuilder withTags(Set<Tag> tags){
        QBook book = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();
        tags.forEach(tag -> builder.and(book.tags.contains(tag)));
        expressions.add(builder);
        return this;
    }

    public BookPredicateBuilder withSearchOperation(SearchCriteria searchParam){
        QBook book = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();

        expressions.add(builder);
        return this;
    }

    public BooleanBuilder build(){
        if(expressions.isEmpty()){
            return new BooleanBuilder(Expressions.asBoolean(true).isTrue());
        }
        BooleanBuilder result = expressions.get(0);
        for (int i = 1; i < expressions.size(); i++) {
            result = result.and(expressions.get(i));
        }
        return result;
    }
}
