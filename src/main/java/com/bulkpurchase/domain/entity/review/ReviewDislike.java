package com.bulkpurchase.domain.entity.review;

import com.bulkpurchase.domain.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "ReviewDislikes")
@Getter
@Setter
public class ReviewDislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ReviewDislikeID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReviewID", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dislikeDate;

}
