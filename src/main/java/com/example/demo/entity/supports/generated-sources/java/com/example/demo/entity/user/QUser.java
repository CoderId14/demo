package com.example.demo.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 954475357L;

    public static final QUser user = new QUser("user");

    public final com.example.demo.entity.QBaseEntity _super = new com.example.demo.entity.QBaseEntity(this);

    public final StringPath avatar = createString("avatar");

    public final ListPath<com.example.demo.entity.ConfirmationToken, com.example.demo.entity.QConfirmationToken> confirmationToken = this.<com.example.demo.entity.ConfirmationToken, com.example.demo.entity.QConfirmationToken>createList("confirmationToken", com.example.demo.entity.ConfirmationToken.class, com.example.demo.entity.QConfirmationToken.class, PathInits.DIRECT2);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isActive = createBoolean("isActive");

    //inherited
    public final StringPath modifiedBy = _super.modifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final EnumPath<com.example.demo.entity.supports.AuthProvider> provider = createEnum("provider", com.example.demo.entity.supports.AuthProvider.class);

    public final StringPath providerId = createString("providerId");

    public final SetPath<com.example.demo.entity.Role, com.example.demo.entity.QRole> roles = this.<com.example.demo.entity.Role, com.example.demo.entity.QRole>createSet("roles", com.example.demo.entity.Role.class, com.example.demo.entity.QRole.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

