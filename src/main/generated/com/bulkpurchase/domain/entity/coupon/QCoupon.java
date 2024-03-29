package com.bulkpurchase.domain.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = -1728871009L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final SetPath<CouponApplicableProduct, QCouponApplicableProduct> applicableProducts = this.<CouponApplicableProduct, QCouponApplicableProduct>createSet("applicableProducts", CouponApplicableProduct.class, QCouponApplicableProduct.class, PathInits.DIRECT2);

    public final StringPath code = createString("code");

    public final NumberPath<Long> couponID = createNumber("couponID", Long.class);

    public final com.bulkpurchase.domain.entity.user.QUser createdBy;

    public final StringPath description = createString("description");

    public final NumberPath<Double> discount = createNumber("discount", Double.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Double> maxDiscountAmount = createNumber("maxDiscountAmount", Double.class);

    public final NumberPath<Double> minimumOrderAmount = createNumber("minimumOrderAmount", Double.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final EnumPath<com.bulkpurchase.domain.enums.CouponType> type = createEnum("type", com.bulkpurchase.domain.enums.CouponType.class);

    public final DateTimePath<java.time.LocalDateTime> validFrom = createDateTime("validFrom", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> validUntil = createDateTime("validUntil", java.time.LocalDateTime.class);

    public QCoupon(String variable) {
        this(Coupon.class, forVariable(variable), INITS);
    }

    public QCoupon(Path<? extends Coupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoupon(PathMetadata metadata, PathInits inits) {
        this(Coupon.class, metadata, inits);
    }

    public QCoupon(Class<? extends Coupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdBy = inits.isInitialized("createdBy") ? new com.bulkpurchase.domain.entity.user.QUser(forProperty("createdBy"), inits.get("createdBy")) : null;
    }

}

