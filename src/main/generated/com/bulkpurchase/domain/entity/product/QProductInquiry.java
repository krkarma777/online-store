package com.bulkpurchase.domain.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductInquiry is a Querydsl query type for ProductInquiry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductInquiry extends EntityPathBase<ProductInquiry> {

    private static final long serialVersionUID = 1627487590L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductInquiry productInquiry = new QProductInquiry("productInquiry");

    public final StringPath inquiryContent = createString("inquiryContent");

    public final DateTimePath<java.util.Date> inquiryDate = createDateTime("inquiryDate", java.util.Date.class);

    public final NumberPath<Long> inquiryID = createNumber("inquiryID", Long.class);

    public final QProduct product;

    public final StringPath replyContent = createString("replyContent");

    public final DateTimePath<java.util.Date> replyDate = createDateTime("replyDate", java.util.Date.class);

    public final com.bulkpurchase.domain.entity.user.QUser user;

    public QProductInquiry(String variable) {
        this(ProductInquiry.class, forVariable(variable), INITS);
    }

    public QProductInquiry(Path<? extends ProductInquiry> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductInquiry(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductInquiry(PathMetadata metadata, PathInits inits) {
        this(ProductInquiry.class, metadata, inits);
    }

    public QProductInquiry(Class<? extends ProductInquiry> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
        this.user = inits.isInitialized("user") ? new com.bulkpurchase.domain.entity.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

