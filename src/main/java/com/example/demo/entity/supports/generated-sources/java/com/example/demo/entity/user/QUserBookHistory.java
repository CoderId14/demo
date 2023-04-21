package com.example.demo.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserBookHistory is a Querydsl query type for UserBookHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserBookHistory extends EntityPathBase<UserBookHistory> {

    private static final long serialVersionUID = -1009675826L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserBookHistory userBookHistory = new QUserBookHistory("userBookHistory");

    public final com.example.demo.entity.QBaseEntity _super = new com.example.demo.entity.QBaseEntity(this);

    public final com.example.demo.entity.book.QBook book;

    public final com.example.demo.entity.chapter.QChapter chapter;

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

    public final QUser user;

    public QUserBookHistory(String variable) {
        this(UserBookHistory.class, forVariable(variable), INITS);
    }

    public QUserBookHistory(Path<? extends UserBookHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserBookHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserBookHistory(PathMetadata metadata, PathInits inits) {
        this(UserBookHistory.class, metadata, inits);
    }

    public QUserBookHistory(Class<? extends UserBookHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.example.demo.entity.book.QBook(forProperty("book"), inits.get("book")) : null;
        this.chapter = inits.isInitialized("chapter") ? new com.example.demo.entity.chapter.QChapter(forProperty("chapter"), inits.get("chapter")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

