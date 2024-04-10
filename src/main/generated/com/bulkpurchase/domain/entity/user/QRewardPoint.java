package com.bulkpurchase.domain.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRewardPoint is a Querydsl query type for RewardPoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRewardPoint extends EntityPathBase<RewardPoint> {

    private static final long serialVersionUID = 388140461L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRewardPoint rewardPoint = new QRewardPoint("rewardPoint");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> earnedDate = createDateTime("earnedDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> expiryDate = createDateTime("expiryDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> RewardPointID = createNumber("RewardPointID", Long.class);

    public final EnumPath<com.bulkpurchase.domain.enums.RewardPointStatus> status = createEnum("status", com.bulkpurchase.domain.enums.RewardPointStatus.class);

    public final DateTimePath<java.time.LocalDateTime> usedDate = createDateTime("usedDate", java.time.LocalDateTime.class);

    public final QUser user;

    public QRewardPoint(String variable) {
        this(RewardPoint.class, forVariable(variable), INITS);
    }

    public QRewardPoint(Path<? extends RewardPoint> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRewardPoint(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRewardPoint(PathMetadata metadata, PathInits inits) {
        this(RewardPoint.class, metadata, inits);
    }

    public QRewardPoint(Class<? extends RewardPoint> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

