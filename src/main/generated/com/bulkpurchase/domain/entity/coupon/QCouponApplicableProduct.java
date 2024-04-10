package com.bulkpurchase.domain.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouponApplicableProduct is a Querydsl query type for CouponApplicableProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponApplicableProduct extends EntityPathBase<CouponApplicableProduct> {

    private static final long serialVersionUID = 1186339953L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouponApplicableProduct couponApplicableProduct = new QCouponApplicableProduct("couponApplicableProduct");

    public final QCoupon coupon;

    public final NumberPath<Long> CouponApplicableProductID = createNumber("CouponApplicableProductID", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public QCouponApplicableProduct(String variable) {
        this(CouponApplicableProduct.class, forVariable(variable), INITS);
    }

    public QCouponApplicableProduct(Path<? extends CouponApplicableProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouponApplicableProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouponApplicableProduct(PathMetadata metadata, PathInits inits) {
        this(CouponApplicableProduct.class, metadata, inits);
    }

    public QCouponApplicableProduct(Class<? extends CouponApplicableProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon"), inits.get("coupon")) : null;
    }

}

