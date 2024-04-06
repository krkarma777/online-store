package com.bulkpurchase.domain.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1246269247L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final StringPath address = createString("address");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath detailAddress = createString("detailAddress");

    public final StringPath email = createString("email");

    public final ListPath<FavoriteProduct, QFavoriteProduct> favoriteProducts = this.<FavoriteProduct, QFavoriteProduct>createList("favoriteProducts", FavoriteProduct.class, QFavoriteProduct.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final SetPath<com.bulkpurchase.domain.entity.product.Product, com.bulkpurchase.domain.entity.product.QProduct> products = this.<com.bulkpurchase.domain.entity.product.Product, com.bulkpurchase.domain.entity.product.QProduct>createSet("products", com.bulkpurchase.domain.entity.product.Product.class, com.bulkpurchase.domain.entity.product.QProduct.class, PathInits.DIRECT2);

    public final StringPath realName = createString("realName");

    public final SetPath<RewardPoint, QRewardPoint> rewardPoints = this.<RewardPoint, QRewardPoint>createSet("rewardPoints", RewardPoint.class, QRewardPoint.class, PathInits.DIRECT2);

    public final EnumPath<com.bulkpurchase.domain.enums.UserRole> role = createEnum("role", com.bulkpurchase.domain.enums.UserRole.class);

    public final EnumPath<com.bulkpurchase.domain.enums.UserStatus> status = createEnum("status", com.bulkpurchase.domain.enums.UserStatus.class);

    public final StringPath userGrade = createString("userGrade");

    public final NumberPath<Long> userID = createNumber("userID", Long.class);

    public final StringPath username = createString("username");

    public final QVerificationToken verificationToken;

    public final StringPath zipCode = createString("zipCode");

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.verificationToken = inits.isInitialized("verificationToken") ? new QVerificationToken(forProperty("verificationToken"), inits.get("verificationToken")) : null;
    }

}

