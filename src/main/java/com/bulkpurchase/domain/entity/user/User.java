package com.bulkpurchase.domain.entity.user;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userID;

    @NotBlank(message = "아이디는 필수 항목입니다.", groups = RegisterCheck.class)
    @Size(min = 3, max = 50, message = "아이디는 3자 이상 50자 이하로 입력해주세요.", groups = RegisterCheck.class)
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @NotBlank(message = "이메일은 필수 항목입니다.", groups = RegisterCheck.class)
    @Email(message = "유효한 이메일 주소를 입력해주세요.", groups = RegisterCheck.class)
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank(message = "실제 이름은 필수 항목입니다.", groups = RegisterCheck.class)
    @Column(nullable = false, length = 100)
    private String realName;

    @NotBlank(message = "비밀번호는 필수 항목입니다.", groups = RegisterCheck.class)
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.", groups = RegisterCheck.class)
    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(length = 20)
    private String userGrade = "1";

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.", groups = RegisterCheck.class)
    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(length = 255)
    private String detailAddress;

    @Column(length = 20)
    private String zipCode;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
        if (userGrade == null) {
            userGrade = "1";
        }
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_sales_regions", joinColumns = @JoinColumn(name = "userid"))
    @Column(name = "region")
    @Enumerated(EnumType.STRING)
    private Set<SalesRegion> salesRegions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Product> products = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.INACTIVE;

    @OneToMany(mappedBy = "user")
    private List<FavoriteProduct> favoriteProducts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RewardPoint> rewardPoints = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private VerificationToken verificationToken;

    public UserStatus getOppositeStatus() {
        return this.status == UserStatus.ACTIVE ? UserStatus.INACTIVE : UserStatus.ACTIVE;
    }
}
