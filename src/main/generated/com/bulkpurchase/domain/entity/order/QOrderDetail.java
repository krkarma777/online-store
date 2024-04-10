package com.bulkpurchase.domain.entity.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderDetail is a Querydsl query type for OrderDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderDetail extends EntityPathBase<OrderDetail> {

    private static final long serialVersionUID = 808604816L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderDetail orderDetail = new QOrderDetail("orderDetail");

    public final QOrder order;

    public final NumberPath<Long> orderDetailID = createNumber("orderDetailID", Long.class);

    public final NumberPath<Double> price = createNumber("price", Double.class);

    public final com.bulkpurchase.domain.entity.product.QProduct product;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final StringPath shippingCompany = createString("shippingCompany");

    public final StringPath shippingNumber = createString("shippingNumber");

    public final EnumPath<com.bulkpurchase.domain.enums.OrderStatus> status = createEnum("status", com.bulkpurchase.domain.enums.OrderStatus.class);

    public QOrderDetail(String variable) {
        this(OrderDetail.class, forVariable(variable), INITS);
    }

    public QOrderDetail(Path<? extends OrderDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderDetail(PathMetadata metadata, PathInits inits) {
        this(OrderDetail.class, metadata, inits);
    }

    public QOrderDetail(Class<? extends OrderDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
        this.product = inits.isInitialized("product") ? new com.bulkpurchase.domain.entity.product.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

