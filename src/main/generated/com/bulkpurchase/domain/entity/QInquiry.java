package com.bulkpurchase.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInquiry is a Querydsl query type for Inquiry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInquiry extends EntityPathBase<Inquiry> {

    private static final long serialVersionUID = 1306324216L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInquiry inquiry = new QInquiry("inquiry");

    public final StringPath inquiryContent = createString("inquiryContent");

    public final DateTimePath<java.time.LocalDateTime> inquiryDate = createDateTime("inquiryDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> inquiryID = createNumber("inquiryID", Long.class);

    public final StringPath responseContent = createString("responseContent");

    public final DateTimePath<java.time.LocalDateTime> responseDate = createDateTime("responseDate", java.time.LocalDateTime.class);

    public final EnumPath<com.bulkpurchase.domain.enums.inquiry.InquiryStatus> status = createEnum("status", com.bulkpurchase.domain.enums.inquiry.InquiryStatus.class);

    public final StringPath title = createString("title");

    public final EnumPath<com.bulkpurchase.domain.enums.inquiry.InquiryType> type = createEnum("type", com.bulkpurchase.domain.enums.inquiry.InquiryType.class);

    public final com.bulkpurchase.domain.entity.user.QUser user;

    public QInquiry(String variable) {
        this(Inquiry.class, forVariable(variable), INITS);
    }

    public QInquiry(Path<? extends Inquiry> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInquiry(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInquiry(PathMetadata metadata, PathInits inits) {
        this(Inquiry.class, metadata, inits);
    }

    public QInquiry(Class<? extends Inquiry> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.bulkpurchase.domain.entity.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

