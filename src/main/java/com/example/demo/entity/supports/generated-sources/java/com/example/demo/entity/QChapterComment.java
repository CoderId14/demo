package com.example.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChapterComment is a Querydsl query type for ChapterComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChapterComment extends EntityPathBase<ChapterComment> {

    private static final long serialVersionUID = 191179683L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChapterComment chapterComment = new QChapterComment("chapterComment");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath body = createString("body");

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

    public final com.example.demo.entity.user.QUser user;

    public QChapterComment(String variable) {
        this(ChapterComment.class, forVariable(variable), INITS);
    }

    public QChapterComment(Path<? extends ChapterComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChapterComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChapterComment(PathMetadata metadata, PathInits inits) {
        this(ChapterComment.class, metadata, inits);
    }

    public QChapterComment(Class<? extends ChapterComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chapter = inits.isInitialized("chapter") ? new com.example.demo.entity.chapter.QChapter(forProperty("chapter"), inits.get("chapter")) : null;
        this.user = inits.isInitialized("user") ? new com.example.demo.entity.user.QUser(forProperty("user")) : null;
    }

}

