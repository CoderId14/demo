package com.example.demo.entity.chapter;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.demo.entity.chapter.ChapterImg;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChapterImg is a Querydsl query type for ChapterImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChapterImg extends EntityPathBase<ChapterImg> {

    private static final long serialVersionUID = 575256136L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChapterImg chapterImg = new QChapterImg("chapterImg");

    public final QChapter chapter;

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final ArrayPath<byte[], Byte> data = createArray("data", byte[].class);

    public final StringPath fileName = createString("fileName");

    public final StringPath fileType = createString("fileType");

    public final StringPath id = createString("id");

    public final NumberPath<Integer> imgNumber = createNumber("imgNumber", Integer.class);

    public final StringPath modifiedBy = createString("modifiedBy");

    public final DateTimePath<java.time.LocalDateTime> modifiedDate = createDateTime("modifiedDate", java.time.LocalDateTime.class);

    public QChapterImg(String variable) {
        this(ChapterImg.class, forVariable(variable), INITS);
    }

    public QChapterImg(Path<? extends ChapterImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChapterImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChapterImg(PathMetadata metadata, PathInits inits) {
        this(ChapterImg.class, metadata, inits);
    }

    public QChapterImg(Class<? extends ChapterImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chapter = inits.isInitialized("chapter") ? new QChapter(forProperty("chapter"), inits.get("chapter")) : null;
    }

}

