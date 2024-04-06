package com.bulkpurchase.domain.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 48221473L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final QCategory category;

    public final StringPath description = createString("description");

    public final ListPath<com.bulkpurchase.domain.entity.user.FavoriteProduct, com.bulkpurchase.domain.entity.user.QFavoriteProduct> favoritedByUsers = this.<com.bulkpurchase.domain.entity.user.FavoriteProduct, com.bulkpurchase.domain.entity.user.QFavoriteProduct>createList("favoritedByUsers", com.bulkpurchase.domain.entity.user.FavoriteProduct.class, com.bulkpurchase.domain.entity.user.QFavoriteProduct.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> imageUrls = this.<String, StringPath>createList("imageUrls", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Double> price = createNumber("price", Double.class);

    public final NumberPath<Long> productID = createNumber("productID", Long.class);

    public final StringPath productName = createString("productName");

    public final SetPath<com.bulkpurchase.domain.enums.SalesRegion, EnumPath<com.bulkpurchase.domain.enums.SalesRegion>> salesRegions = this.<com.bulkpurchase.domain.enums.SalesRegion, EnumPath<com.bulkpurchase.domain.enums.SalesRegion>>createSet("salesRegions", com.bulkpurchase.domain.enums.SalesRegion.class, EnumPath.class, PathInits.DIRECT2);

    public final EnumPath<com.bulkpurchase.domain.enums.ProductStatus> status = createEnum("status", com.bulkpurchase.domain.enums.ProductStatus.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public final com.bulkpurchase.domain.entity.user.QUser user;

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category"), inits.get("category")) : null;
        this.user = inits.isInitialized("user") ? new com.bulkpurchase.domain.entity.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

