package com.example.demo.entity.book;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.demo.entity.book.Book;
import com.example.demo.entity.chapter.Chapter;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -1655773667L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBook book = new QBook("book");

    public final com.example.demo.entity.QBaseEntity _super = new com.example.demo.entity.QBaseEntity(this);

    public final SetPath<com.example.demo.entity.Category, com.example.demo.entity.QCategory> categories = this.<com.example.demo.entity.Category, com.example.demo.entity.QCategory>createSet("categories", com.example.demo.entity.Category.class, com.example.demo.entity.QCategory.class, PathInits.DIRECT2);

    public final ListPath<Chapter, com.example.demo.entity.chapter.QChapter> chapters = this.<Chapter, com.example.demo.entity.chapter.QChapter>createList("chapters", Chapter.class, com.example.demo.entity.chapter.QChapter.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

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

    public final StringPath shortDescription = createString("shortDescription");

    public final SetPath<com.example.demo.entity.Tag, com.example.demo.entity.QTag> tags = this.<com.example.demo.entity.Tag, com.example.demo.entity.QTag>createSet("tags", com.example.demo.entity.Tag.class, com.example.demo.entity.QTag.class, PathInits.DIRECT2);

    public final com.example.demo.entity.QAttachment thumbnail;

    public final StringPath title = createString("title");

    public final com.example.demo.entity.user.QUser user;

    public final ListPath<com.example.demo.entity.user.UserBookHistory, com.example.demo.entity.user.QUserBookHistory> userBookHistories = this.<com.example.demo.entity.user.UserBookHistory, com.example.demo.entity.user.QUserBookHistory>createList("userBookHistories", com.example.demo.entity.user.UserBookHistory.class, com.example.demo.entity.user.QUserBookHistory.class, PathInits.DIRECT2);

    public QBook(String variable) {
        this(Book.class, forVariable(variable), INITS);
    }

    public QBook(Path<? extends Book> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBook(PathMetadata metadata, PathInits inits) {
        this(Book.class, metadata, inits);
    }

    public QBook(Class<? extends Book> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.thumbnail = inits.isInitialized("thumbnail") ? new com.example.demo.entity.QAttachment(forProperty("thumbnail")) : null;
        this.user = inits.isInitialized("user") ? new com.example.demo.entity.user.QUser(forProperty("user")) : null;
    }

}

