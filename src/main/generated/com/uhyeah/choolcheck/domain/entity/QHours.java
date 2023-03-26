package com.uhyeah.choolcheck.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHours is a Querydsl query type for Hours
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHours extends EntityPathBase<Hours> {

    private static final long serialVersionUID = -1015303940L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHours hours = new QHours("hours");

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final StringPath title = createString("title");

    public final QUser user;

    public QHours(String variable) {
        this(Hours.class, forVariable(variable), INITS);
    }

    public QHours(Path<? extends Hours> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHours(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHours(PathMetadata metadata, PathInits inits) {
        this(Hours.class, metadata, inits);
    }

    public QHours(Class<? extends Hours> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

