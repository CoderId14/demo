package com.example.demo.entity.chapter;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.demo.entity.chapter.Chapter;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChapter is a Querydsl query type for Chapter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChapter extends EntityPathBase<Chapter> {

    private static final long serialVersionUID = -160009349L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChapter chapter = new QChapter("chapter");

    public final com.example.demo.entity.QBaseEntity _super = new com.example.demo.entity.QBaseEntity(this);

    public final com.example.demo.entity.book.QBook book;

    public final ListPath<com.example.demo.entity.ChapterComment, com.example.demo.entity.QChapterComment> chapterComment = this.<com.example.demo.entity.ChapterComment, com.example.demo.entity.QChapterComment>createList("chapterComment", com.example.demo.entity.ChapterComment.class, com.example.demo.entity.QChapterComment.class, PathInits.DIRECT2);

    public final NumberPath<Integer> chapterNumber = createNumber("chapterNumber", Integer.class);

    public final StringPath content = createString("content");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath title = createString("title");

    public final ListPath<com.example.demo.entity.user.UserBookHistory, com.example.demo.entity.user.QUserBookHistory> userBookHistories = this.<com.example.demo.entity.user.UserBookHistory, com.example.demo.entity.user.QUserBookHistory>createList("userBookHistories", com.example.demo.entity.user.UserBookHistory.class, com.example.demo.entity.user.QUserBookHistory.class, PathInits.DIRECT2);

    public QChapter(String variable) {
        this(Chapter.class, forVariable(variable), INITS);
    }

    public QChapter(Path<? extends Chapter> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChapter(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChapter(PathMetadata metadata, PathInits inits) {
        this(Chapter.class, metadata, inits);
    }

    public QChapter(Class<? extends Chapter> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.example.demo.entity.book.QBook(forProperty("book"), inits.get("book")) : null;
    }

}

