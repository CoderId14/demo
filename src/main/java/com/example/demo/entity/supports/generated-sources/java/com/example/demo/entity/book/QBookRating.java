package com.example.demo.entity.book;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.demo.entity.book.BookRating;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookRating is a Querydsl query type for BookRating
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookRating extends EntityPathBase<BookRating> {

    private static final long serialVersionUID = 1402583578L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookRating bookRating = new QBookRating("bookRating");

    public final com.example.demo.entity.QBaseEntity _super = new com.example.demo.entity.QBaseEntity(this);

    public final QBook book;

    public final StringPath comment = createString("comment");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final com.example.demo.entity.user.QUser user;

    public QBookRating(String variable) {
        this(BookRating.class, forVariable(variable), INITS);
    }

    public QBookRating(Path<? extends BookRating> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookRating(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookRating(PathMetadata metadata, PathInits inits) {
        this(BookRating.class, metadata, inits);
    }

    public QBookRating(Class<? extends BookRating> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book"), inits.get("book")) : null;
        this.user = inits.isInitialized("user") ? new com.example.demo.entity.user.QUser(forProperty("user")) : null;
    }

}

