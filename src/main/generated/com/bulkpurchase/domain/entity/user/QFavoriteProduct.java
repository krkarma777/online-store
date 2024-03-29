package com.bulkpurchase.domain.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFavoriteProduct is a Querydsl query type for FavoriteProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavoriteProduct extends EntityPathBase<FavoriteProduct> {

    private static final long serialVersionUID = -1366212673L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFavoriteProduct favoriteProduct = new QFavoriteProduct("favoriteProduct");

    public final DateTimePath<java.time.LocalDateTime> favoritedAt = createDateTime("favoritedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.bulkpurchase.domain.entity.product.QProduct product;

    public final QUser user;

    public QFavoriteProduct(String variable) {
        this(FavoriteProduct.class, forVariable(variable), INITS);
    }

    public QFavoriteProduct(Path<? extends FavoriteProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFavoriteProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFavoriteProduct(PathMetadata metadata, PathInits inits) {
        this(FavoriteProduct.class, metadata, inits);
    }

    public QFavoriteProduct(Class<? extends FavoriteProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.bulkpurchase.domain.entity.product.QProduct(forProperty("product"), inits.get("product")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

