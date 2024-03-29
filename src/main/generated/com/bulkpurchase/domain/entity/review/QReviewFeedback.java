package com.bulkpurchase.domain.entity.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewFeedback is a Querydsl query type for ReviewFeedback
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewFeedback extends EntityPathBase<ReviewFeedback> {

    private static final long serialVersionUID = 70904324L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewFeedback reviewFeedback = new QReviewFeedback("reviewFeedback");

    public final DateTimePath<java.util.Date> feedbackDate = createDateTime("feedbackDate", java.util.Date.class);

    public final EnumPath<com.bulkpurchase.domain.enums.FeedbackType> feedbackType = createEnum("feedbackType", com.bulkpurchase.domain.enums.FeedbackType.class);

    public final QReview review;

    public final NumberPath<Long> ReviewFeedbackID = createNumber("ReviewFeedbackID", Long.class);

    public final com.bulkpurchase.domain.entity.user.QUser user;

    public QReviewFeedback(String variable) {
        this(ReviewFeedback.class, forVariable(variable), INITS);
    }

    public QReviewFeedback(Path<? extends ReviewFeedback> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewFeedback(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewFeedback(PathMetadata metadata, PathInits inits) {
        this(ReviewFeedback.class, metadata, inits);
    }

    public QReviewFeedback(Class<? extends ReviewFeedback> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.user = inits.isInitialized("user") ? new com.bulkpurchase.domain.entity.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

