package com.bulkpurchase.domain.entity.discount;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGlobalDiscount is a Querydsl query type for GlobalDiscount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGlobalDiscount extends EntityPathBase<GlobalDiscount> {

    private static final long serialVersionUID = -1549617214L;

    public static final QGlobalDiscount globalDiscount = new QGlobalDiscount("globalDiscount");

    public final StringPath description = createString("description");

    public final NumberPath<Double> discount = createNumber("discount", Double.class);

    public final EnumPath<com.bulkpurchase.domain.enums.DiscountType> discountType = createEnum("discountType", com.bulkpurchase.domain.enums.DiscountType.class);

    public final NumberPath<Long> globalDiscountID = createNumber("globalDiscountID", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Double> maxDiscountAmount = createNumber("maxDiscountAmount", Double.class);

    public final NumberPath<Double> minimumOrderAmount = createNumber("minimumOrderAmount", Double.class);

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> validFrom = createDateTime("validFrom", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> validUntil = createDateTime("validUntil", java.time.LocalDateTime.class);

    public QGlobalDiscount(String variable) {
        super(GlobalDiscount.class, forVariable(variable));
    }

    public QGlobalDiscount(Path<? extends GlobalDiscount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGlobalDiscount(PathMetadata metadata) {
        super(GlobalDiscount.class, metadata);
    }

}

